package PetriTool;

import static PetriTool.PetriTool.getPlaceLabel;
import static PetriTool.PetriTool.getTransitionLabel;

import java.nio.channels.NonWritableChannelException;
import java.util.Vector;

import static PetriTool.PetriTool.designPanel_;
import static PetriTool.PetriTool.componentPanel_;

public class PetriModule {
	private Place placeOk;
	private Place placeFail;
	private Place placeExe;
	private Place placeSuccess;
	private Place placeError;
	private Place placeBegin;
	private Place placeAbort;
	private Place placeStop;
	private Transition transitionOk;
	private Transition transitionError;
	private Transition transitionAbort;
	private Transition transitionBegin;
	
	
	private Arc arcExeToOk;
	private Arc arcExeToError;
	private Arc arcOkToOk;
	private Arc arcOkToSuccess;
	private Arc arcErrorToError;
	private Arc arcErrorToFail;
	private Arc arcBeginToBegin;
	private Arc arcBeginToExe;
	private Arc arcAbortToAbort;
	private Arc arcAbortToStop;
	private Arc arcExeToAbort;
	
	private int XCoordinate;
	private int YCoordinate;
	
	public PetriModule(int x, int y) {
		XCoordinate=x;
		YCoordinate=y;
		placeExe = new Place(x,y,getPlaceLabel());
		placeOk = new Place(x+4,y-3,getPlaceLabel());
		placeError = new Place(x+4,y+3,getPlaceLabel());
		placeSuccess = new Place(x+8,y-1,getPlaceLabel());
		placeFail = new Place(x+8,y+1,getPlaceLabel());
		placeBegin = new Place(x-4,y-2,getPlaceLabel());
		placeAbort = new Place(x-4,y+5,getPlaceLabel());
		placeStop = new Place(x+8,y+5,getPlaceLabel());
		transitionBegin = new Transition(x-4,y,getTransitionLabel());
		transitionAbort = new Transition(x+2,y+5,getTransitionLabel());
		transitionOk = new Transition(x+4, y-1, getTransitionLabel());
		transitionError = new Transition(x+4, y+1, getTransitionLabel());
		changeNames();
		setArc();
//		setArcEnd();
//		arcAssemble();
//		printArc();
		putIntoVectors();
	}
	
	
	public Place getExe() {
		return placeExe;
	}
	
	public int getXCoordinate() {
		return XCoordinate;
	}

	public static final int xDistance = 12;
	public static final int yDistance = 8;
	

	public int getYCoordinate() {
		return YCoordinate;
	}


	private static int moduleCount = 0;
	
	private void changeNames() {
		placeExe.setplaceName_("ComP"+moduleCount);
		placeOk.setplaceName_("ComP"+moduleCount+"_Ok");
		placeSuccess.setplaceName_("ComP"+moduleCount+"_Success");
		placeError.setplaceName_("ComP"+moduleCount+"_Error");
		placeFail.setplaceName_("ComP"+moduleCount+"_Fail");
		placeAbort.setplaceName_("ComP"+moduleCount+"_Abort");
		placeBegin.setplaceName_("ComP"+moduleCount+"_Begin");
		placeStop.setplaceName_("ComP"+moduleCount+"_Stop");
		transitionOk.setTransitionName_("ComT"+moduleCount+"_Ok");
		transitionBegin.setTransitionName_("ComT"+moduleCount+"_Begin");
		transitionAbort.setTransitionName_("ComT"+moduleCount+"_Abort");
		transitionError.setTransitionName_("ComT"+moduleCount+"_Error");
		moduleCount++;
	}
	
	

	/**
	 * put places and transitions into vectors
	 */
	private void putIntoVectors() {
		designPanel_.placeVector_.add(placeExe);
		designPanel_.placeVector_.add(placeOk);
		designPanel_.placeVector_.add(placeError);
		designPanel_.placeVector_.add(placeSuccess);
		designPanel_.placeVector_.add(placeFail);
		designPanel_.placeVector_.add(placeAbort);
		designPanel_.placeVector_.add(placeBegin);
		designPanel_.placeVector_.add(placeStop);
		
		designPanel_.transitionVector_.add(transitionOk);
		designPanel_.transitionVector_.add(transitionAbort);
		designPanel_.transitionVector_.add(transitionBegin);
		designPanel_.transitionVector_.add(transitionError);
		
		designPanel_.arcVector_.add(arcBeginToBegin);
		designPanel_.arcVector_.add(arcBeginToExe);
		designPanel_.arcVector_.add(arcAbortToAbort);
		designPanel_.arcVector_.add(arcErrorToError);
		designPanel_.arcVector_.add(arcExeToOk);
		designPanel_.arcVector_.add(arcErrorToFail);
		designPanel_.arcVector_.add(arcOkToOk);
		designPanel_.arcVector_.add(arcExeToAbort);
		designPanel_.arcVector_.add(arcAbortToStop);
		designPanel_.arcVector_.add(arcExeToError);
		designPanel_.arcVector_.add(arcOkToSuccess);
	}
	
	private void setArc() {
		arcBeginToBegin = new Arc(placeBegin,transitionBegin);
		arcBeginToExe = new Arc(transitionBegin,placeExe);
		arcAbortToAbort = new Arc(placeAbort,transitionAbort);
		arcErrorToError = new Arc(placeError,transitionError);
		arcExeToOk = new Arc(placeExe,transitionOk);
		arcErrorToFail = new Arc(transitionError,placeFail);
		arcOkToOk = new Arc(placeOk,transitionOk);
		arcExeToAbort = new Arc(placeExe,transitionAbort);
		arcAbortToStop = new Arc(transitionAbort,placeStop);
		arcExeToError = new Arc(placeExe,transitionError);
		arcOkToSuccess = new Arc(transitionOk,placeSuccess);
	}
}
	