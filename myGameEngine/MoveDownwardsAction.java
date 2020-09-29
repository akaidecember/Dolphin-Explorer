package myGameEngine;

import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import ray.rage.game.*;
import ray.rml.*;
import a1.MyGame;
import net.java.games.input.Event;

//Class declaration for MoveUpwardsAction
public class MoveDownwardsAction extends AbstractInputAction {

    private Camera camera;
    private MyGame myGame;

    public MoveDownwardsAction(Camera c, MyGame g) {
    	
        camera = c;
        myGame = g;
        
    }

	public void performAction(float time, Event e) {
		
		if(myGame.camera.getMode() == 'c'){ 
			
			//Console output
			System.out.println("Downwards Action off the dolphin");
			Vector3f v   = camera.getUp();
			Vector3f p   = camera.getPo();
			Vector3f p1  = (Vector3f)Vector3f.createFrom(-0.15f*v.x(), -0.15f*v.y(), -0.15f*v.z());
			Vector3f p2  = (Vector3f)p.add((Vector3)p1);
			camera.setPo((Vector3f)Vector3f.createFrom(p2.x(), p2.y(),p2.z()));
			
		}
		else{		

			//Console output
			System.out.println("Downwards Action riding the dolphin");
			myGame.dolphinNode.moveUp(-0.15f);
			
		}
	
	}
    
}