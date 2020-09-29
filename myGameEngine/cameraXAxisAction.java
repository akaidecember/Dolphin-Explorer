package myGameEngine;

import a1.MyGame;
import net.java.games.input.Event;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.Camera;
import ray.rml.Vector3;
import ray.rml.Vector3f;

public class cameraXAxisAction extends AbstractInputAction {
	
	private Camera cam;
	private MyGame myGame;
	
	public cameraXAxisAction(Camera c, MyGame g) {
		
		cam    = c;
		myGame = g;
		
	}
	
	public void performAction(float time, Event e) {
		
		if(e.getValue() <= -0.2f){
			
			if(myGame.camera.getMode() == 'c') {      
				
				//Console output
				System.out.println("Gamepad camera X axis left off dolphin");
				Vector3f v = cam.getFd();
				Vector3f p  = cam.getPo();
				Vector3f p1 = (Vector3f)Vector3f.createFrom(0.15f*v.x(),0.15f*v.y(), 0.15f*v.z());
				Vector3f p2 = (Vector3f)p.add((Vector3)p1);
				cam.setPo((Vector3f)Vector3f.createFrom(p2.x(), p2.y(),p2.z()));
				
			}
			else{
				
				//console output
				System.out.println("Gamepad camera X axis left on the dolphin");
				myGame.dolphinNode.moveForward(0.15f);
				
			}
			
		}
		else if(e.getValue() >= 0.2f){
			
			if(myGame.camera.getMode() == 'c'){ 
				
				//console output
				System.out.println("Gamepad camera X axis right off the dolphin");
				Vector3f v = cam.getFd();
				Vector3f p = cam.getPo();
				Vector3f p1 = (Vector3f)Vector3f.createFrom(-0.15f*v.x(),-0.15f*v.y(), -0.15f*v.z());
				Vector3f p2 = (Vector3f)p.add((Vector3)p1);
				cam.setPo((Vector3f)Vector3f.createFrom(p2.x(), p2.y(),p2.z()));
				
			}
			else{					    
				
				// if the view mode is set to 'n' (ON DOLPHIN)
				System.out.println("Gamepad camera X axis right on the dolphin");
				myGame.dolphinNode.moveBackward(0.15f);
				
			}
			
		}
		
	}
	
}