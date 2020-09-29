package myGameEngine;

import a1.MyGame;
import net.java.games.input.Event;
import ray.input.action.AbstractInputAction;
import ray.rml.Vector3;
import ray.rml.Vector3f;

public class RideToggleAction extends AbstractInputAction{
	
	private MyGame myGame;
	
	public RideToggleAction(MyGame g){
		
		myGame = g;
		
	}

	public void performAction(float time, Event e){
		
		if(myGame.rideDolphin == false){
			
			myGame.rideDolphin = true;
			myGame.camera.setMode('c');
			Vector3 dolphinLocation = myGame.dolphinNode.getLocalPosition();
			Vector3f p1 = (Vector3f)Vector3f.createFrom(0.4f, -0.2f, 0.2f); 
			Vector3f p2 = (Vector3f)dolphinLocation.add((Vector3)p1);
			myGame.camera.setPo((Vector3f)p2);

			//Console output
			System.out.println("Off the dolphin");	
			
		}
		else{
			
			myGame.rideDolphin = false;
			myGame.camera.setMode('n');
			System.out.println("On the dolphin");
			
		}
		
	}
	
}