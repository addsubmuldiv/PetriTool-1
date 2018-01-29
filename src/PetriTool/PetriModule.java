package PetriTool;

import static PetriTool.PetriTool.getPlaceLabel;
import static PetriTool.PetriTool.getTransitionLabel;

import java.util.Vector;

import static PetriTool.PetriTool.designPanel_;
import static PetriTool.PetriTool.componentPanel_;

public class PetriModule {
	private Place placeOk;
	private Place placeFail;
	private Place placeAction;
	private Place placeSuccess;
	private Place placeError;
	private Transition transitionOk;
	private Transition transitionError;
	
	
	private Arc arcActionToOk;
	private Arc arcActionToError;
	private Arc arcOkToOk;
	private Arc arcOkToSuccess;
	private Arc arcErrorToError;
	private Arc arcErrorToFail;
	
	
	
	public PetriModule(int x, int y) {
		placeAction = new Place(x,y,getPlaceLabel());
		placeOk = new Place(x+4,y-3,getPlaceLabel());
		placeError = new Place(x+4,y+3,getPlaceLabel());
		placeSuccess = new Place(x+8,y-1,getPlaceLabel());
		placeFail = new Place(x+8,y+1,getPlaceLabel());
		transitionOk = new Transition(x+4, y-1, getTransitionLabel());
		transitionError = new Transition(x+4, y+1, getTransitionLabel());
		changeNames();
		putIntoVectors();
		setArcStart();
		setArcEnd();
		arcAssemble();
	}
	
	
	private static int moduleCount = 0;
	
	private void changeNames() {
		placeAction.setplaceName_("mP"+moduleCount);
		placeOk.setplaceName_("mP"+moduleCount+"_Ok");
		placeSuccess.setplaceName_("mP"+moduleCount+"_Success");
		placeError.setplaceName_("mP"+moduleCount+"_Error");
		placeFail.setplaceName_("mP"+moduleCount+"_Fail");
		transitionOk.setTransitionName_("mT"+moduleCount+"_Ok");
		transitionError.setTransitionName_("mT"+moduleCount+"_Fail");
		moduleCount++;
	}

	/**
	 * put places and transitions into vectors
	 */
	private void putIntoVectors() {
		designPanel_.placeVector_.add(placeAction);
		designPanel_.placeVector_.add(placeOk);
		designPanel_.placeVector_.add(placeError);
		designPanel_.placeVector_.add(placeSuccess);
		designPanel_.placeVector_.add(placeFail);
		
		designPanel_.transitionVector_.add(transitionOk);
		designPanel_.transitionVector_.add(transitionError);
	}
	
	private void setArcStart() {
		arcActionToOk = new Arc(placeAction.getXCoordinate(),placeAction.getYCoordinate());
		arcActionToError = new Arc(placeAction.getXCoordinate(),placeAction.getYCoordinate());
		arcOkToOk = new Arc(placeOk.getXCoordinate(),placeOk.getYCoordinate());
		arcOkToSuccess = new Arc(transitionOk.getXCoordinate(),transitionOk.getYCoordinate());
		arcErrorToError = new Arc(placeError.getXCoordinate(),placeError.getYCoordinate());
		arcErrorToFail = new Arc(transitionError.getXCoordinate(),transitionError.getYCoordinate());
		
		arcActionToOk.setStartComponent("Place");
		arcActionToError.setStartComponent("Place");
		arcOkToOk.setStartComponent("Place");
		arcErrorToError.setStartComponent("Place");
		arcOkToSuccess.setStartComponent("Transition");
		arcErrorToFail.setStartComponent("Transition");
	}
	
	private void setArcEnd() {
		arcActionToOk.addCoordinates(transitionOk.getXCoordinate(), transitionOk.getYCoordinate());
		arcOkToOk.addCoordinates(transitionOk.getXCoordinate(), transitionOk.getYCoordinate());
		arcActionToError.addCoordinates(transitionError.getXCoordinate(), transitionError.getYCoordinate());
		arcErrorToError.addCoordinates(transitionError.getXCoordinate(), transitionError.getYCoordinate());
		arcErrorToFail.addCoordinates(placeFail.getXCoordinate(), placeFail.getYCoordinate());
		arcOkToSuccess.addCoordinates(placeSuccess.getXCoordinate(), placeSuccess.getYCoordinate());
		
		arcActionToOk.setEndComponent("Transition");
		arcActionToError.setEndComponent("Transition");
		arcErrorToError.setEndComponent("Transition");
		arcOkToOk.setEndComponent("Transition");
		arcOkToSuccess.setEndComponent("Place");
		arcErrorToFail.setEndComponent("Place");
	}
	
	private void arcAssemble() {
		Vector<Arc> tempArcVector = new Vector<>();
		tempArcVector.add(arcActionToError);
		tempArcVector.add(arcActionToOk);
		tempArcVector.add(arcErrorToError);
		tempArcVector.add(arcErrorToFail);
		tempArcVector.add(arcOkToSuccess);
		tempArcVector.add(arcOkToOk);
			
		for(Arc arc : tempArcVector) {
			arc.setTokensToEnable(componentPanel_.getTokensToEnable());
			arc.calculateSlopes();
			arc.setArcDrawCoordinates();
			designPanel_.arcVector_.add(arc);
		}
	}
}