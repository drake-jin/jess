package BestFirst;

public class PuzzleNode {
	int layerNum;

    int distance;

	int nodeNum;

	int openTmpStat[] = new int[9];

	String path;
	
    //Best First Search using 
	public PuzzleNode(StringBuffer cPath, int dis, int nNum, int oTmpStat[]) {
		path = cPath.toString();
		distance = dis;
		nodeNum = nNum;
		System.arraycopy(oTmpStat, 0, openTmpStat, 0, 9);
	}
	
    //Depth and Breadth First Search using
	public PuzzleNode(StringBuffer cPath, int oTmpStat[], int nNum, int lNum) {
		path = cPath.toString();
		System.arraycopy(oTmpStat, 0, openTmpStat, 0, 9);
		nodeNum = nNum;
		layerNum = lNum;
	}

	public PuzzleNode(int cloTmpStat[]) {
		System.arraycopy(cloTmpStat, 0, openTmpStat, 0, 9);
	}
}
