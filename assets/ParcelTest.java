

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.test.ActivityInstrumentationTestCase2;


import com.games.test1.*;
import com.games.test1.GameView.GameThread;
import com.games.test1.aal.AALExecutionState;
import com.games.test1.aal.AALValue;

public class ParcelTest extends ActivityInstrumentationTestCase2<TestOne> {

	private Bundle mBundle;
	

	public ParcelTest() {
		super("com.games.test1", TestOne.class);
	}
	
	public void testSanity() {
		assertTrue(true);
	}
	
	public void testShouldParcelObjectData() {
		ObjectData d = new ObjectData(1,2,3,4,"test","obj");
		
		Parcel p = Parcel.obtain();
		d.writeToParcel(p, 0);
		p.setDataPosition(0);
		
		ObjectData t = ObjectData.CREATOR.createFromParcel(p);
		assertTrue(t.getX() == 1);
		assertTrue(t.getY() == 2);
		assertTrue(t.getWidth() == 3);
		assertTrue(t.getHeight() == 4);
		assertTrue(t.getSpriteID().equals("test"));
		assertTrue(t.getObjectID().equals("obj"));		
	}
	
	public void testShouldBundleObjectData() {
		Bundle b = new Bundle();
		ObjectData d = new ObjectData(1,2,3,4,"test","obj");
		b.putParcelable("object", d);
		
		ObjectData t = (ObjectData)b.getParcelable("object");
		assertTrue(t.getX() == 1);
		assertTrue(t.getY() == 2);
		assertTrue(t.getWidth() == 3);
		assertTrue(t.getHeight() == 4);
		assertTrue(t.getSpriteID().equals("test"));
		assertTrue(t.getObjectID().equals("obj"));		
	}
	
	public void testShouldBundleValue() {
		Bundle b = new Bundle();	
		b.putParcelable("test1", new AALValue(1));
		b.putParcelable("test2", new AALValue(1.0f));
		b.putParcelable("test3", new AALValue(true));
		b.putParcelable("test4", new AALValue("pass"));
		
		assertTrue(b.getParcelable("test1").equals(new AALValue(1)));
		assertTrue(b.getParcelable("test2").equals(new AALValue(1.0f)));
		assertTrue(b.getParcelable("test3").equals(new AALValue(true)));
		assertTrue(b.getParcelable("test4").equals(new AALValue("pass")));
	}
	
	public void testShouldBundleScene() {
		Scene s = new Scene();
		DrawnObject ball = new ForegroundObject(new Animation(), 32, 55);
		ball.setID("ball");
		
		s.addObject(ball);
		
		Bundle b = new Bundle();
		s.saveToBundle(b);
		
		Scene ts = new Scene();
		// reset coordinates -- should be loaded.
		ball.setX(0);
		ball.setY(0);
		assertTrue(ball.getX() == 0);
		
		ts.addObject(ball);
		ts.loadFromBundle(b);
		
		assertTrue(ball.getX() == 32);
		assertTrue(ball.getY() == 55);		
	}
	
	public void testShouldBundleExecutionState() {
		AALExecutionState es = new AALExecutionState();
		es.setVariable("myvar", new AALValue(4));
		es.setVariable("myvar2", new AALValue("test"));
		
		Bundle b = new Bundle();
		es.saveToBundle(b);
		
		AALExecutionState tes = new AALExecutionState();
		tes.loadFromBundle(b);
		
		assertTrue(tes.getVariable("myvar").equals(new AALValue(4)));
		assertTrue(tes.getVariable("myvar2").equals(new AALValue("test")));
	}
	
	public void testShouldReloadGame() {
		Activity mActivity = this.getActivity();
		GameView view = (GameView) mActivity.findViewById(com.games.test1.R.id.gameview);		
		GameThread game = view.getThread();		
		
		game.getCurrentScene().moveObject("jar", 66, 66);
		
		mBundle = new Bundle();
		game.saveToBundle(mBundle);
		
		mActivity.finish();
		
				
		mActivity = this.getActivity();
	    view = (GameView) mActivity.findViewById(com.games.test1.R.id.gameview);		
	    game = view.getThread();		
	    game.getExecutor().startGame();
	    
		
		assertTrue(game.getCurrentScene().getObjectFromID("jar").getX() != 66);
		assertTrue(game.getCurrentScene().getObjectFromID("jar").getY() != 66);
		
		game.loadFromBundle(mBundle);
		
		assertTrue(game.getCurrentScene().getObjectFromID("jar").getX() == 66);
		assertTrue(game.getCurrentScene().getObjectFromID("jar").getY() == 66);
	}
	
	public void testShouldConvertBundleToHashMap() {
		Bundle b = new Bundle();
		b.putInt("one", 4);
		b.putInt("two", 10); 
		
		// Test a sub-bundle.
		Bundle sb = new Bundle();
		sb.putFloat("test", 5.0f);
		
		b.putBundle("subbundle", sb);
		
		
		// Convert to a hashmap and test it.
		HashMap h = Utility.convertBundleToHashMap(b);
		assertTrue((Integer) h.get("one") == 4);
		assertTrue((Integer) h.get("two") == 10);
		HashMap sh = (HashMap)h.get("subbundle");
		assertTrue((Float) sh.get("test") == 5.0f);		
	}
	
	public void testShouldConvertHashMapToBundle() {
		HashMap<String, Serializable> h = new HashMap<String, Serializable>(),
				sh = new HashMap<String, Serializable>();
		h.put("test", 4);
		h.put("test2", 5.0f);
		sh.put("inner", "test");
		h.put("submap", sh);
		
		Bundle b = Utility.convertHashMapToBundle(h);
		
		assertTrue(b.getInt("test") == 4);
		assertTrue(b.getFloat("test2") == 5.0f);
		Bundle sb = (Bundle) b.getParcelable("submap");
		assertTrue(sb.getString("inner").equals("test"));
	}
	
	public void testShouldSerializeAndUnserializeHashMap() {
		HashMap<String, Serializable> h = new HashMap<String, Serializable>(),
		sh = new HashMap<String, Serializable>();
		h.put("test", 4);
		h.put("test2", 5.0f);
		sh.put("inner", "test");
		h.put("submap", sh);
		
		try {
			FileOutputStream fos =  this.getActivity().openFileOutput("test.txt", Activity.MODE_PRIVATE);
			ObjectOutputStream s = new ObjectOutputStream(fos);
			s.writeObject(h);
			s.close();
			fos.close();
		} catch (Exception e) {
			assertTrue(false);
		}
		
		HashMap<String, Serializable> th = null;
		try {
			FileInputStream fos = this.getActivity().openFileInput("test.txt");
			ObjectInputStream s = new ObjectInputStream(fos);
			th = (HashMap<String, Serializable>) s.readObject();
			s.close();
			fos.close();
		} catch (Exception e) {
			assertTrue(false);
		}
		
		assertTrue((Integer) th.get("test") == 4);
		assertTrue((Float) th.get("test2") == 5.0f);
		assertTrue(((String)((HashMap<String, Serializable>) th.get("submap")).get("inner")).equals("test"));
	}
	
	public void testShouldSerializeAndUnserializeBundleViaHashMap() {
		Bundle b = new Bundle();
		b.putInt("one", 4);
		b.putInt("two", 10); 
		
		Bundle sb = new Bundle();
		sb.putFloat("test", 5.0f);
		
		b.putBundle("subbundle", sb);
		
		
		HashMap<String, Serializable> h = Utility.convertBundleToHashMap(b);
		
		try {
			FileOutputStream fos =  this.getActivity().openFileOutput("test.txt", Activity.MODE_PRIVATE);
			ObjectOutputStream s = new ObjectOutputStream(fos);
			s.writeObject(h);
			s.close();
			fos.close();
		} catch (Exception e) {
			assertTrue(false);
		}
		
		HashMap<String, Serializable> th = null;
		try {
			FileInputStream fos = this.getActivity().openFileInput("test.txt");
			ObjectInputStream s = new ObjectInputStream(fos);
			th = (HashMap<String, Serializable>) s.readObject();
			s.close();
			fos.close();
		} catch (Exception e) {
			assertTrue(false);
		}
		
		Bundle tb = Utility.convertHashMapToBundle(th);
		
		assertTrue((Integer) b.getInt("one") == (Integer) tb.getInt("one"));	
	}
	
	
	public void testShouldReloadGameFromFile() {
		Activity mActivity = this.getActivity();
		GameView view = (GameView) mActivity.findViewById(com.games.test1.R.id.gameview);		
		GameThread game = view.getThread();		
		
		game.getCurrentScene().moveObject("jar", 66, 66);
		
		
		game.saveToFile("test.save");
		
		mActivity.finish();
		
				
		mActivity = this.getActivity();
	    view = (GameView) mActivity.findViewById(com.games.test1.R.id.gameview);		
	    game = view.getThread();		
	    game.getExecutor().startGame();
	    
		
		assertTrue(game.getCurrentScene().getObjectFromID("jar").getX() != 66);
		assertTrue(game.getCurrentScene().getObjectFromID("jar").getY() != 66);
		
		game.loadFromFile("test.save");
		
		assertTrue(game.getCurrentScene().getObjectFromID("jar").getX() == 66);
		assertTrue(game.getCurrentScene().getObjectFromID("jar").getY() == 66);
	}
}
