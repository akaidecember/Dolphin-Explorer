package myGameEngine;

import a1.MyGame;
import net.java.games.input.Event;
import ray.input.action.AbstractInputAction;
import ray.rml.Angle;
import ray.rml.Degreef;
import ray.rml.Vector3;
import ray.rml.Vector3f;

public class PanLeftRightAction extends AbstractInputAction{
	
	private MyGame myGame;
	
	public PanLeftRightAction(MyGame g){
		
		myGame = g;
		
	}

	public void performAction(float time, Event e){
		
		if(e.getValue() >= 0.2f){
			
			if(myGame.camera.getMode()=='n'){
				
		       Angle deg = Degreef.createFrom(-1.0f);
		       myGame.dolphinNode.yaw(deg);
		       
		       //console output
		       System.out.println("Pan right off dolphin");      
		       
			}
			else{
				
		       Angle deg  = Degreef.createFrom(-1.0f);
		       Vector3 uVector = myGame.camera.getRt();
		       Vector3 vVector = myGame.camera.getUp();
		       Vector3 nVector = myGame.camera.getFd();
		       Vector3 uTransform = (uVector.rotate(deg, vVector)).normalize();
		       Vector3 nTransform = (nVector.rotate(deg, vVector)).normalize();
		       myGame.camera.setRt((Vector3f)uTransform);
		       myGame.camera.setFd((Vector3f)nTransform);
		       
		       //Console output
		       System.out.println("Pan right on the dolphin");  
		       
			}
			
		}
		else if(e.getValue() <= -0.2f){
			
			if(myGame.camera.getMode()=='n'){
				
			       Angle deg = Degreef.createFrom(1.0f);
			       myGame.dolphinNode.yaw(deg);
			       
			       //Console output
			       System.out.println("Pan left on the dolphin");
			       
			}
			else{
				
			       Angle deg  = Degreef.createFrom(1.0f);
			       Vector3 uVector = myGame.camera.getRt();
			       Vector3 vVector = myGame.camera.getUp();
			       Vector3 nVector = myGame.camera.getFd();
			       Vector3 uTransform = (uVector.rotate(deg, vVector)).normalize();
			       Vector3 nTransform = (nVector.rotate(deg, vVector)).normalize();
			       myGame.camera.setRt((Vector3f)uTransform);
			       myGame.camera.setFd((Vector3f)nTransform);
			       
			       //Console output
			       System.out.println("Pan left on the dolphin");
			       
			}
		}
		
	}
	
}