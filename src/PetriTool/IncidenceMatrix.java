package PetriTool;

import java.util.Vector;

public class IncidenceMatrix {
	
	
	private Vector<Place> placeVector;
	private Vector<Transition> transitionVector;
	private Vector<Arc> arcVector;
	
	
	public static int[][] incidenceMatrix;
	
	
	public IncidenceMatrix() {
		this.placeVector = PetriTool.designPanel_.placeVector_;
		this.transitionVector = PetriTool.designPanel_.transitionVector_;
		this.arcVector = PetriTool.designPanel_.arcVector_;
		incidenceMatrix = new int[placeVector.size()][transitionVector.size()];
		//TODO 
		assembleMatrix();
		System.out.println("the matrix is");
		output();
	}
	
	
	
	public void output() {
		for(int i=0;i<incidenceMatrix.length;i++) {
			for(int j=0;j<incidenceMatrix[0].length;j++) {
				System.out.print(incidenceMatrix[i][j]+" ");
			}
			System.out.println();
		}
	}
		
	public void assembleMatrix() {
		for(Arc arc: arcVector) {
			if(arc.startComponent_.equals("Place")) {
				Place tempP = arc.getStartPlace_();
				Transition tempT = arc.getDestinationTransition_();
				int row = 0, col = 0;
				for(int i=0;i<placeVector.size();i++) {
					if(placeVector.get(i)==tempP) {
						row = i;
					}
				}
				for(int i=0;i<transitionVector.size();i++) {
					if(transitionVector.get(i)==tempT) {
						col = i;
					}
				}
				incidenceMatrix[row][col] = -1 * arc.getTokensToEnable();
			} else {
				Place tempP = arc.getDestinationPlace_();
				Transition tempT = arc.getStartTransition_();
				int row = 0, col = 0;
				for(int i=0;i<placeVector.size();i++) {
					if(placeVector.get(i)==tempP) {
						row = i;
					}
				}
				for(int i=0;i<transitionVector.size();i++) {
					if(transitionVector.get(i)==tempT) {
						col = i;
					}
				}
				incidenceMatrix[row][col] = arc.getTokensToEnable();
			}
		}
	}
	
	
}
