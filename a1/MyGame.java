/*
 * This is the MyGame class for the Dolphin Explorer mini-game. This class contains the main function and all the other game related setups that are 
 * necessary for the operation of the game. This file also contains the  instances for the objects that are present in the game world. 
 * 
 * Assignment #1 for CSC 165
 * Professor Scott Gordon
 * CSUS
 * 
 * @author Anshul Kumar Shandilya
 * 
 * */

package a1;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.*;
import java.util.*;
import java.util.ArrayList;
import net.java.games.input.Controller;

import ray.rage.*;
import ray.rage.game.*;
import ray.rage.rendersystem.*;
import ray.rage.rendersystem.Renderable.*;
import ray.rage.scene.*;
import ray.rage.scene.Camera.Frustum.*;
import ray.rage.scene.controllers.*;
import ray.rml.*;
import ray.rage.rendersystem.gl4.GL4RenderSystem;

import ray.rage.rendersystem.states.*;
import ray.rage.asset.material.Material;
import ray.rage.asset.texture.*;
import ray.input.*;
import ray.input.action.*;
import ray.input.action.AbstractInputAction;
import ray.rage.game.*;
import net.java.games.input.Event;

import ray.rage.rendersystem.shader.*;
import ray.rage.util.*;
import myGameEngine.*;

//Class declaration for MyGame
public class MyGame extends VariableFrameRateGame {

	GL4RenderSystem rs;
	float elapsTime = 0.0f;
	String elapsTimeStr, planetsVisitedStr, dispStr, energyLeft, gameOverStr;
    int elapsTimeSec, planetsVisited = 0, energy = 100, counter = 0;
    
    //Private variables for the class MyGame
    private InputManager im;
    private Action quitGameAction, moveForwardAction, moveBackwardAction, moveLeftAction, moveRightAction, moveUpAction, moveDownAction, rotateRightAction, rotateLeftAction, rotateUpAction, rotateDownAction, rideToggleAction, cameraY, cameraX, cameraPitch, cameraPan, visitPlanetAction;
    public Camera camera;
    public boolean rideDolphin = true;
    public SceneNode dolphinNode;
    private boolean visitPlanetA = false, visitPlanetB = false, visitPlanetC = false, depleted = false, freeze = false;
    private SceneNode planetANode, planetBNode, planetCNode, cameraNode, pyramidNode;								//Is there a way to make it private and access from action classes?
    	
    //Random Function for randomly positioning the planets
    Random rand = new Random();

    //Constructor for the class MyGame
    public MyGame() {
    	
        super();
        
    }

    //Main function for the program/game
    public static void main(String[] args) {
    	
        Game game = new MyGame();
        
        try {
        	
            game.startup();
            game.run();
            
        } catch (Exception e) {
        	
            e.printStackTrace(System.err);
            
        } finally {
        	
            game.shutdown();
            game.exit();
            
        }
        
    }
    
	@Override
	protected void setupWindow(RenderSystem rs, GraphicsEnvironment ge) {
		
		rs.createRenderWindow(new DisplayMode(1000, 700, 24, 60), false);
		
	}

    @Override
    protected void setupCameras(SceneManager sm, RenderWindow rw) {
    	
        SceneNode rootNode = sm.getRootSceneNode();
        camera = sm.createCamera("MainCamera", Projection.PERSPECTIVE);
        rw.getViewport(0).setCamera(camera);
		camera.setRt((Vector3f)Vector3f.createFrom(1.0f, 0.0f, 0.0f));
		camera.setUp((Vector3f)Vector3f.createFrom(0.0f, 1.0f, 0.0f));
		camera.setFd((Vector3f)Vector3f.createFrom(0.0f, 0.0f, -1.0f));
        camera.setPo((Vector3f)Vector3f.createFrom(0.0f, 0.0f, 0.0f));
        cameraNode = rootNode.createChildSceneNode(camera.getName() + "Node");
        cameraNode.attachObject(camera);
        
    }
	
    @Override
    protected void setupScene(Engine eng, SceneManager sm) throws IOException {
    	
    	//Calling the function to setup the inputs
        setupInputs();

        //Creating a dolphin
        Entity dolphinE = sm.createEntity("myDolphin", "dolphinHighPoly.obj");
        dolphinE.setPrimitive(Primitive.TRIANGLES);
        dolphinNode = sm.getRootSceneNode().createChildSceneNode(dolphinE.getName() + "Node");
        dolphinNode.moveBackward(2.0f);
        dolphinNode.attachObject(dolphinE);
        dolphinNode.attachObject(camera);
        
        //Creating the camera
        SceneNode dolphinCamera = dolphinNode.createChildSceneNode("myDolphin");
        dolphinCamera.moveUp(0.3f);
        dolphinCamera.moveUp(0.3f);
        dolphinCamera.moveBackward(0.299f);
        dolphinCamera.moveRight(0.01f);
        dolphinCamera.attachObject(camera);

        //Setting up the planets
        Entity planetA = sm.createEntity("PlanetA", "earth.obj");
        planetA.setPrimitive(Primitive.TRIANGLES);
        Entity planetB = sm.createEntity("PlanetB", "sphere.obj");
        planetB.setPrimitive(Primitive.TRIANGLES);
        Entity planetC = sm.createEntity("PlanetC", "sphere.obj");
        planetC.setPrimitive(Primitive.TRIANGLES);
        
        //Attaching Planets to Node Object, assigning positions, and scaling
        //For planet A
        planetANode = sm.getRootSceneNode().createChildSceneNode(planetA.getName() + "Node");
        planetANode.attachObject(planetA);
        float[] positions = new float[] {-30f, 30f, -30f, 30f, -20f, 20f};
        planetRandomPosition(planetANode, positions);
        planetANode.setLocalScale(2f, 2f, 2f);

        //For planet B
        planetBNode = sm.getRootSceneNode().createChildSceneNode(planetB.getName() + "Node");
        planetBNode.attachObject(planetB);
        positions = new float[]{-45f, 45f, -45f, 45f, -35f, 35f};
        planetRandomPosition(planetBNode, positions);
        planetBNode.setLocalScale(3f, 3f, 3f);

        //For Planet C
        planetCNode = sm.getRootSceneNode().createChildSceneNode(planetC.getName() + "Node");
        planetCNode.attachObject(planetC);
        positions = new float[] {-0f, 2f, 0f, 5f, -75f, -50f};
        planetRandomPosition(planetCNode, positions);
        planetCNode.setLocalScale(12f, 12f, 12f);

        TextureManager tm = eng.getTextureManager();
        Texture planetBTexture = tm.getAssetByPath("earth-day.jpeg");
        RenderSystem rs = sm.getRenderSystem();
        TextureState state = (TextureState) rs.createRenderState(RenderState.Type.TEXTURE);
        state.setTexture(planetBTexture);
        planetB.setRenderState(state);

        tm = eng.getTextureManager();
        Texture planetCTexture = tm.getAssetByPath("earth-day.jpeg");
        rs = sm.getRenderSystem();
        state = (TextureState) rs.createRenderState(RenderState.Type.TEXTURE);
        state.setTexture(planetCTexture);
        planetC.setRenderState(state);

        //Create axis
        SceneNode manualObj = sm.getRootSceneNode().createChildSceneNode("ManualObjectNode");
        SceneNode axisNode = manualObj.createChildSceneNode("AxisNode");
        
        //x-axis
        ManualObject lineX = this.makeAxeX(eng, sm);
        SceneNode lineXNode = axisNode.createChildSceneNode("lineXNode");
        lineXNode.attachObject(lineX);
        //yy-axis
        ManualObject lineY = this.makeAxeY(eng, sm);
        SceneNode lineYNode = axisNode.createChildSceneNode("lineYNode");
        lineYNode.attachObject(lineY);
        //z-axis
        ManualObject lineZ = this.makeAxeZ(eng, sm);
        SceneNode lineZNode = axisNode.createChildSceneNode("lineZNode");
        lineZNode.attachObject(lineZ);
        
        //Creating pyramid as a home base to refill energy
        ManualObject pyramid = makePyramid(eng, sm);
        pyramidNode = sm.getRootSceneNode().createChildSceneNode("PyrNode");
        pyramidNode.scale(0.75f, 0.75f, 0.75f);
        pyramidNode.setLocalPosition(10.0f, 10.0f, -30.0f);
        pyramidNode.attachObject(pyramid);
        
        //Cause Node Objects to Rotate
        //For the dolphin, set value to 0f for it to stop rotating
        RotationController rcDolphin = new RotationController(Vector3f.createUnitVectorY(), .0f);
        rcDolphin.addNode(dolphinNode);
        RotationController rcPlanetA = new RotationController(Vector3f.createUnitVectorY(), .02f);
        rcPlanetA.addNode(planetANode);
        RotationController rcPlanetB = new RotationController(Vector3f.createUnitVectorX(), .01f);
        rcPlanetB.addNode(planetBNode);
        RotationController rcPlanetC = new RotationController(Vector3f.createUnitVectorY(), .005f);
        rcPlanetC.addNode(planetCNode);
        
        dolphinNode.yaw(Degreef.createFrom(180.0f));

        sm.addController(rcDolphin);
        sm.addController(rcPlanetA);
        sm.addController(rcPlanetB);
        sm.addController(rcPlanetC);
    
        // Set up Lights
        sm.getAmbientLight().setIntensity(new Color(.3f, .3f, .3f));
		Light plight = sm.createLight("testLamp1", Light.Type.POINT);
		plight.setAmbient(new Color(.1f, .1f, .1f));
        plight.setDiffuse(new Color(.7f, .7f, .7f));
		plight.setSpecular(new Color(1.0f, 1.0f, 1.0f));
        plight.setRange(100f);
		SceneNode plightNode = sm.getRootSceneNode().createChildSceneNode("plightNode");
        plightNode.attachObject(plight);
        plightNode.setLocalPosition(1.0f, 1.0f, 5.0f);  

        
    }

    //Function to make the pyramid
    private ManualObject makePyramid(Engine eng, SceneManager sm) throws IOException {

        ManualObject pyramid = sm.createManualObject("Pyramid");
        ManualObjectSection pyrSec = pyramid.createManualSection("PyramidSection");
        pyramid.setGpuShaderProgram(sm.getRenderSystem().getGpuShaderProgram(GpuShaderProgram.Type.RENDERING));
        float[] vertices = new float[]
            { -1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f, 0.0f, 1.0f, 0.0f, //front
            1.0f, -1.0f, 1.0f, 1.0f, -1.0f, -1.0f, 0.0f, 1.0f, 0.0f, //right
            1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, 0.0f, 1.0f, 0.0f, //back
            -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, 1.0f, 0.0f, 1.0f, 0.0f, //left
            -1.0f, -1.0f, -1.0f, 1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, //LF
            1.0f, -1.0f, 1.0f, -1.0f, -1.0f, -1.0f, 1.0f, -1.0f, -1.0f //RR
            };
        float[] texcoords = new float[]
            { 0.0f, 0.0f, 1.0f, 0.0f, 0.5f, 1.0f,
            0.0f, 0.0f, 1.0f, 0.0f, 0.5f, 1.0f,
            0.0f, 0.0f, 1.0f, 0.0f, 0.5f, 1.0f,
            0.0f, 0.0f, 1.0f, 0.0f, 0.5f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f
            };
        float[] normals = new float[]
            { 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f,
            0.0f, 1.0f, -1.0f, 0.0f, 1.0f, -1.0f, 0.0f, 1.0f, -1.0f,
            -1.0f, 1.0f, 0.0f, -1.0f, 1.0f, 0.0f, -1.0f, 1.0f, 0.0f,
            0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f,
            0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f
            };

        int[] indices = new int[] { 0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17};
        FloatBuffer vertBuf = BufferUtil.directFloatBuffer(vertices);
        FloatBuffer texBuf = BufferUtil.directFloatBuffer(texcoords);
        FloatBuffer normBuf = BufferUtil.directFloatBuffer(normals);
        IntBuffer indexBuf = BufferUtil.directIntBuffer(indices);
        pyrSec.setVertexBuffer(vertBuf);
        pyrSec.setTextureCoordsBuffer(texBuf);
        pyrSec.setNormalsBuffer(normBuf);
        pyrSec.setIndexBuffer(indexBuf);
        Texture tex = eng.getTextureManager().getAssetByPath("chain-fence.jpeg");
        TextureState texState = (TextureState) sm.getRenderSystem().createRenderState(RenderState.Type.TEXTURE);
        texState.setTexture(tex);
        FrontFaceState faceState = (FrontFaceState) sm.getRenderSystem().createRenderState(RenderState.Type.FRONT_FACE);
        pyramid.setDataSource(DataSource.INDEX_BUFFER);
        pyramid.setRenderState(texState);
        pyramid.setRenderState(faceState);
        
        return pyramid;
            
    }

	//Function to setup inputs for various actions
    protected void setupInputs(){ 
    	
        im = new GenericInputManager();
        //Get the list of all the input devices available
        ArrayList<Controller> controllers = im.getControllers();
        
        moveForwardAction = new MoveForwardsAction(camera, this);						//camera forward
        moveBackwardAction = new MoveBackwardsAction(camera, this);						//camera backward
        moveLeftAction = new MoveLeftAction(camera, this);								//camera left
        moveRightAction = new MoveRightAction(camera, this);							//camera right
        moveUpAction = new MoveUpwardsAction(camera, this);								//camera upwards
        moveDownAction = new MoveDownwardsAction(camera, this);							//camera downwards
        rotateRightAction = new RotateCameraRightAction(this);							//Pan right
        rotateLeftAction = new RotateCameraLeftAction(this);							//Pan left
        rotateUpAction = new RotateCameraUpAction(this);								//Pitch up
        rotateDownAction = new RotateCameraDownAction(this);							//Pitch Down
        cameraY = new cameraYAxisAction(camera, this);									//Camera Y axis for XB1 controller
        cameraX = new cameraXAxisAction(camera, this);									//Camera X axis for XB1 controller
        cameraPitch = new PitchUpDownAction(this);										//Camera pitch up and down action for XB1 controller 
        cameraPan = new PanLeftRightAction(this);										//Camera pan left and right action for XB1 controller
        rideToggleAction = new RideToggleAction(this);									//To toggle the on and off dolphin action

        //Error checking to check if the controllers are connected or not (ensuring the game does not crash)
        for (Controller c : controllers) {
        	
        	//If the controller type is keyboard, then use the keyboard controls, otherwise use the gamepad controls
            if (c.getType() == Controller.Type.KEYBOARD)
                keyboardControls(c);													//Call the keyboard =Control function to handle the keyboard inputs
            else if (c.getType() == Controller.Type.GAMEPAD || c.getType() == Controller.Type.STICK)
                gamepadControls(c);														//Call the gamepad input to control the XB1 inputs
            
        }
        
    }
    
    //Function to handle the gamepad controlls
    void gamepadControls(Controller gpName) {
    	
    	im.associateAction(gpName, net.java.games.input.Component.Identifier.Button._0, rideToggleAction, InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);  
    	im.associateAction(gpName, net.java.games.input.Component.Identifier.Axis.Y, cameraY, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);  
    	im.associateAction(gpName, net.java.games.input.Component.Identifier.Axis.X, cameraX, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN); 
    	im.associateAction(gpName, net.java.games.input.Component.Identifier.Axis.RX, cameraPan, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
    	im.associateAction(gpName, net.java.games.input.Component.Identifier.Axis.RY, cameraPitch, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN); 
    	im.associateAction(gpName, net.java.games.input.Component.Identifier.Button._4, moveUpAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);  
    	im.associateAction(gpName, net.java.games.input.Component.Identifier.Button._5, moveDownAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);  

    }

    //Function to handle the keyboard controls
    void keyboardControls(Controller kbName) {
		
        im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.D, moveLeftAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.A, moveRightAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.W, moveForwardAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.S, moveBackwardAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.LEFT, rotateLeftAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.RIGHT, rotateRightAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.DOWN, rotateUpAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.UP, rotateDownAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.Q, quitGameAction, InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
        im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.SPACE, rideToggleAction, InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
        
    }

    @Override
    protected void update(Engine engine) {
    	
    	if(depleted == true) {
    		
    		System.out.println("Energy exhausted, freezing game!");
    		
    		//measures for a case where energy is exhausted
	    	rs = (GL4RenderSystem) engine.getRenderSystem();
	    	rs.setHUD("Game Over!............. You ran out of energy............");
	    		
	    	//Freeze the game once out of energy
	    	while(!freeze) {
	    			
	    		freeze = true;
	    		im.update(elapsTime);
	    		
	    	}
    	
    	}
    	else {
    		
			//Build and set HUD
			rs = (GL4RenderSystem) engine.getRenderSystem();
			elapsTime += engine.getElapsedTimeMillis();
			elapsTimeSec = Math.round(elapsTime/1000.0f);
			elapsTimeStr = Integer.toString(elapsTimeSec);
			//Energy mechanism
			counter++;
			if(counter > 100) {
				
				energy--;
				counter = 0;
				if(energy <= 0)
					depleted = true;
				
			}
			energyLeft = Integer.toString(energy);
			planetsVisitedStr = Integer.toString(planetsVisited);
			dispStr = "Time Elapsed: " + elapsTimeStr + "             Score : " + planetsVisitedStr + "             Energy Left: " + energyLeft;
	        rs.setHUD(dispStr, 12, 12);
	       
	        //Function to check for collision
	        checkForCollision();
	        
	        //Function to respawn the player near the dolphin if hew has gone too far
	        checkPlayerBoundaries();
	        
	        //Check refuelCollision
	        checkRefuelCollision();
	        
	        im.update(elapsTime);																	//*** Important ***
        
    	}
        
/*        //Testing though printf (kid mode :D)
        System.out.println("\n--------------------------------------------------------------------------");
        System.out.printf("\nDolphin x copordinates are:   %s and the camera x coordinates are:   %s\n", dolphinNode.getLocalPosition().x(), camera.getPo().x());
        System.out.printf("\nDolphin y copordinates are:   %s and the camera y coordinates are:   %s\n", dolphinNode.getLocalPosition().y(), camera.getPo().y());
        System.out.printf("\nDolphin z copordinates are:   %s and the camera z coordinates are:   %s\n", dolphinNode.getLocalPosition().z(), camera.getPo().z());
        System.out.println("\n--------------------------------------------------------------------------");
        */
    }

    //Function to check if the player has colided with the home base to be able to refuel
	private void checkRefuelCollision() {
		// TODO Auto-generated method stub
		
		//You will only be able to regain energy if you are off the dolphin
		if(camera.getMode() == 'c') {
			
			if(this.collideRefuel(camera, pyramidNode)) {
				
				//Console output
				System.out.println("Player regained Energy from homebase");
				energy = 100;

			}
		}
		
	}

	//Function similar to the collide function, but with different values
	private boolean collideRefuel(Camera node1, SceneNode node2) {
		
		Vector3 node1Pos = node1.getPo();
		if((Math.abs(node1Pos.x() - node2.getLocalPosition().x()) < 1.0f) && (Math.abs(node1Pos.y() - node2.getLocalPosition().y()) < 1.0f) && (Math.abs(node1Pos.z() - node2.getLocalPosition().z()) < 1.0f)) {
			
			//Console output
			System.out.println("Refueled");
			return true;
			
		}
		else
			return false;

	}

	//Function to check the boundaries of the player. No more than 15.0f away from the dolphin allowed.
    private void checkPlayerBoundaries() {
		// TODO Auto-generated method stub
    	
    	//Only need to check for boundaries if the player is off the dolphin
    	if(camera.getMode() == 'c') {
    		
    		
    		if((Math.abs(camera.getPo().x() - dolphinNode.getLocalPosition().x()) > 15.0f) || (Math.abs(camera.getPo().y() - dolphinNode.getLocalPosition().y()) > 15.0f) || (Math.abs(camera.getPo().z() - dolphinNode.getLocalPosition().z()) > 15.0f)) {
    			
    			camera.setPo((Vector3f) dolphinNode.getLocalPosition());
    			
    			//Console output
    			System.out.println("Player respawned near the dolphin");
    		
    		}
    		
    	}
		
	}

	//This method checks for collision between the player and the planets
    public void checkForCollision(){

    	//Only check for collision if the player is off the camera
    	if(camera.getMode() == 'c') {
    		
			if(visitPlanetA == false) {
				
				if(this.collide(camera, planetANode)) {
					
					System.out.println("Planet A visited");
					visitPlanetA = true;
					incrementCounter(1);
					
				}
				
			}
			if(visitPlanetB == false) {
			
				if(this.collide(camera, planetBNode)) {
				
					System.out.println("Planet B visited");
					visitPlanetB = true;
					incrementCounter(1);
				
				}
			
			}
			if(visitPlanetC == false) {
		
				if(this.collide(camera, planetCNode)) {
			
					System.out.println("Planet C visited");
					visitPlanetC = true;
					incrementCounter(1);
				
				}
		
			}
			
    	}
		
	}

    //This function will do the necessary checks for collision detection
	private boolean collide(Camera node1, SceneNode node2) {
		
		Vector3 node1Pos = node1.getPo();
		if((Math.abs(node1Pos.x() - node2.getLocalPosition().x()) < 7.0f) && (Math.abs(node1Pos.y() - node2.getLocalPosition().y()) < 7.0f) && (Math.abs(node1Pos.z() - node2.getLocalPosition().z()) < 7.0f)) {
			
			//Console output
			System.out.println("Collision occured");
			return true;
			
		}
		else {
			
			//System.out.printf("%s\n\n%s", node1Pos.toString(), node2Pos.toString());
			return false;
			
		}

	}

	//This function will return a random set of values within a range
    float getRandomPosition(float min, float max) {
    	
        return (min + rand.nextFloat() * (max - min));
        
    }
    
    //This function will randomly select the positions for the planets based on the getRandomPosition() function
    void planetRandomPosition(SceneNode node, float[] positions) {
    	
        node.setLocalPosition(getRandomPosition(positions[0], positions[1]), getRandomPosition(positions[2], positions[3]), getRandomPosition(positions[4], positions[5]));

    }

    //This function will increment the counter planetsVisited when called
    public void incrementCounter(int num) {
    	
        planetsVisited += num;
        
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
    	
        Entity dolphin = getEngine().getSceneManager().getEntity("myDolphin");
        
        switch (e.getKeyCode()) {
            case KeyEvent.VK_L:
                dolphin.setPrimitive(Primitive.LINES);
                break;
            case KeyEvent.VK_T:
                dolphin.setPrimitive(Primitive.TRIANGLES);
                break;
            case KeyEvent.VK_P:
                dolphin.setPrimitive(Primitive.POINTS);
                break;
        }
        super.keyPressed(e);
    }
    
	protected ManualObject makeAxeX(Engine eng, SceneManager sm) throws IOException {
		
		float[] vertices = new float[] { 0.0f, 0.0f, 0.0f, 15.0f, 0.0f, 0.0f };
		int[] indices = new int[] { 0, 1 };
		
		ManualObject axe = sm.createManualObject("AxesX");
		ManualObjectSection axeSec = axe.createManualSection("AxesSectionX");
		axe.setGpuShaderProgram(sm.getRenderSystem().getGpuShaderProgram(GpuShaderProgram.Type.RENDERING));
		axe.setPrimitive(Primitive.LINES);
		FloatBuffer vertBuf = BufferUtil.directFloatBuffer(vertices);
		IntBuffer indexBuf = BufferUtil.directIntBuffer(indices);
		axeSec.setVertexBuffer(vertBuf);
		axeSec.setIndexBuffer(indexBuf);
		axe.setDataSource(DataSource.INDEX_BUFFER);
		Material mat = sm.getMaterialManager().getAssetByPath("default.mtl");
		mat.setEmissive(Color.BLUE);
		Texture tex = eng.getTextureManager().getAssetByPath("red.jpeg");
		TextureState tstate = (TextureState) sm.getRenderSystem().createRenderState(RenderState.Type.TEXTURE);
		tstate.setTexture(tex);
		axeSec.setRenderState(tstate);
		axeSec.setMaterial(mat);
		return axe;
		
	}

	protected ManualObject makeAxeY(Engine eng, SceneManager sm) throws IOException {
		
		float[] vertices = new float[] { 0.0f, 0.0f, 0.0f, 0.0f, 15.0f, 0.0f };
		int[] indices = new int[] { 0, 1 };
		
		ManualObject axe = sm.createManualObject("AxesY");
		ManualObjectSection axeSec = axe.createManualSection("AxesSectionY");
		axe.setGpuShaderProgram(sm.getRenderSystem().getGpuShaderProgram(GpuShaderProgram.Type.RENDERING));
		axe.setPrimitive(Primitive.LINES);
		FloatBuffer vertBuf = BufferUtil.directFloatBuffer(vertices);
		IntBuffer indexBuf = BufferUtil.directIntBuffer(indices);
		axeSec.setVertexBuffer(vertBuf);
		axeSec.setIndexBuffer(indexBuf);
		axe.setDataSource(DataSource.INDEX_BUFFER);
		Material mat = sm.getMaterialManager().getAssetByPath("default.mtl");
		mat.setEmissive(Color.BLUE);
		Texture tex = eng.getTextureManager().getAssetByPath("blue.jpeg");
		TextureState tstate = (TextureState) sm.getRenderSystem().createRenderState(RenderState.Type.TEXTURE);
		tstate.setTexture(tex);
		axeSec.setRenderState(tstate);
		axeSec.setMaterial(mat);
		return axe;
		
	}

	protected ManualObject makeAxeZ(Engine eng, SceneManager sm) throws IOException {
		
		float[] vertices = new float[] { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 15.0f };
		int[] indices = new int[] { 0, 1 };
		
		ManualObject axe = sm.createManualObject("AxesZ");
		ManualObjectSection axeSec = axe.createManualSection("AxesSectionZ");
		axe.setGpuShaderProgram(sm.getRenderSystem().getGpuShaderProgram(GpuShaderProgram.Type.RENDERING));
		axe.setPrimitive(Primitive.LINES);
		FloatBuffer vertBuf = BufferUtil.directFloatBuffer(vertices);
		IntBuffer indexBuf = BufferUtil.directIntBuffer(indices);
		axeSec.setVertexBuffer(vertBuf);
		axeSec.setIndexBuffer(indexBuf);
		axe.setDataSource(DataSource.INDEX_BUFFER);
		Material mat = sm.getMaterialManager().getAssetByPath("default.mtl");
		mat.setEmissive(Color.BLUE);
		Texture tex = eng.getTextureManager().getAssetByPath("hexagons.jpeg");
		TextureState tstate = (TextureState) sm.getRenderSystem().createRenderState(RenderState.Type.TEXTURE);
		tstate.setTexture(tex);
		axeSec.setRenderState(tstate);
		axeSec.setMaterial(mat);
		return axe;
		
	}

}



