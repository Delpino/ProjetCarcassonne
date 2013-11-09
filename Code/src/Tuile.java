import java.util.ArrayList;


public class Tuile {
	private Terrain [] tabCarac; // 0 nord, 1 est, 2 sud, 3 ouest, 4 centre
	private boolean [] tabPresenceChamps; // 0 NNO, 1 NNE, 2 ENE, 3 ESE, 4 SSE, 5 SSO, 6 OSO, 7 ONO
	private boolean [][] tabConnexit�Bordure;
	private boolean [][] tabConnexit�Champs;
	private int bouclier; // 0 nord, 1 est, 2 sud, 3 ouest, 4 centre, 5 pas de bouclier.
	private Pion PionPlac�;
	private ArrayList<Tuile> tuileAdjacentes;
	private int sensTuile;
	
	
	public Tuile(Terrain [] caracs, boolean [] presenceChamps, boolean [][] connexiteBordure, boolean [][] connexiteChamps, int bouclier){
		// pr�-requis : Terrain [5] terre;
		// boolean [8] presencechamps;
		// boolean [4][4] connexit�bordure;
		// boolean [8][8] connexit�champs;
		this.tabCarac=caracs;
		this.tabPresenceChamps=presenceChamps;
		this.tabConnexit�Bordure=connexiteBordure;
		this.tabConnexit�Champs=connexiteChamps;
		this.bouclier=bouclier;
		
	}
	
	public void retirePion(){
		this.PionPlac�=null;
	}
	
	public void rotation(int sens){
		//pr�-requis sens 1 : sens horaire ou sens -1 : sens antihoraire  
		if(sens == -1)
			sens = 3; // Bricolage pour le %4 si on a sens = -1
		
		this.sensTuile = (this.sensTuile + sens) % 4;
		
		Terrain temp=this.tabCarac[0];
		this.tabCarac[sens%4]=this.tabCarac[(1+sens)%4];
		this.tabCarac[(1+sens)%4]=this.tabCarac[(2+sens)%4];
		this.tabCarac[(2+sens)%4]=this.tabCarac[(3+sens)%4];
		this.tabCarac[(3+sens)%4]=temp;
		
		if (sens == 1) {
			sens = 2;
		} else if (sens == 3) {
			sens = 6;
		}
		
		boolean temp2 =this.tabPresenceChamps[0];
		this.tabPresenceChamps[sens%8] = this.tabPresenceChamps[(1+sens)%8];
		this.tabPresenceChamps[(1+sens)%8] = this.tabPresenceChamps[(2+sens)%8];
		this.tabPresenceChamps[(2+sens)%8] = this.tabPresenceChamps[(3+sens)%8];
		this.tabPresenceChamps[(3+sens)%8] = this.tabPresenceChamps[(4+sens)%8];
		this.tabPresenceChamps[(4+sens)%8] = this.tabPresenceChamps[(5+sens)%8];
		this.tabPresenceChamps[(5+sens)%8] = this.tabPresenceChamps[(6+sens)%8];
		this.tabPresenceChamps[(6+sens)%8] = this.tabPresenceChamps[(7+sens)%8];
		this.tabPresenceChamps[(7+sens)%8] = temp2;
	}
	
	public boolean verifPoseTuileLegale (ArrayList<Tuile> adjacente, int sens){
		// dans l'attribut ArrayList<Tuile> tuileAdjacentes, l'ordre correspond � leur place par rapport � la tuile qu'on veut poser : 0 = dessus, 1 = droite, 2 = dessous, 3 = � gauche
		if(  (this.tabCarac[0]==adjacente.get(0).tabCarac[2] || adjacente.get(0)==null)
				&& (this.tabCarac[1]==adjacente.get(1).tabCarac[3] || adjacente.get(1)==null)
				&& (this.tabCarac[2]==adjacente.get(2).tabCarac[0] || adjacente.get(2)==null) 
				&& (this.tabCarac[3]==adjacente.get(3).tabCarac[1] || adjacente.get(3)==null) ){
		
		return true;
		}
		else return false;
	}

	
	
	public ArrayList<Tuile> verifPresenceAbbaye (){
		// Fonctions � appliquer sur la tuile qui vient d'�tre pos� et apr�s que le pion a �t� plac� ( ou pas )
		//V�rifie si il y a une abbaye sur la tuile et/ou sur les tuile autour ( adjacente et diagonale ) et l'ajoute dans un ArrayList si c'est le cas
		//Rappel : dans l'attribut ArrayList<Tuile> tuileAdjacentes, l'ordre correspond � leur place par rapport � la tuile qu'on vient de poser : 0 = dessus, 1 = droite, 2 = dessous, 3 = � gauche
		
		
		ArrayList<Tuile> TuileAbbaye = new ArrayList<Tuile>();
		
		if(this.tabCarac[4]==Terrain.ABBAYE){ //pour la tuile qui vient d'�tre pos� 
			TuileAbbaye.add(this);
		}
		if(this.tuileAdjacentes.get(0)!=null){ // pour la tuile du dessus
			if((this.tuileAdjacentes.get(0)).tabCarac[4]==Terrain.ABBAYE){
				TuileAbbaye.add(this.tuileAdjacentes.get(0));
			}
			
		}
		if(this.tuileAdjacentes.get(1)!=null){ // pour la tuile de droite
			if((this.tuileAdjacentes.get(1)).tabCarac[4]==Terrain.ABBAYE){
				TuileAbbaye.add(this.tuileAdjacentes.get(1));
			}
			
		}
		if(this.tuileAdjacentes.get(2)!=null){ // pour la tuile du dessous
			if((this.tuileAdjacentes.get(2)).tabCarac[4]==Terrain.ABBAYE){
				TuileAbbaye.add(this.tuileAdjacentes.get(2));
			}
			
		}
		if(this.tuileAdjacentes.get(3)!=null){ // pour la tuile de gauche
			if((this.tuileAdjacentes.get(3)).tabCarac[4]==Terrain.ABBAYE){
				TuileAbbaye.add(this.tuileAdjacentes.get(3));
			}
			
		}
		
		// les 8 if suivant v�rifient la presence d'abbaye sur les diagonales (2 if par diagonale car 2 chemin pour y acc�der), mais ne peut d�tecter une abbaye qui est en haut � gauche de la tuile pos� si celle-ci n'a
		// ni tuile au-dessus et � gauche d'elle. Mais pas de probl�me car dans ce cas l'abbaye est forc�ment imcompl�te et donc rien � faire.
		
		if( this.tuileAdjacentes.get(0)!=null && this.tuileAdjacentes.get(0).tuileAdjacentes.get(3)!=null){ // diagonale haut/gauche en passant par le haut
			if (this.tuileAdjacentes.get(0).tuileAdjacentes.get(3).tabCarac[4]==Terrain.ABBAYE) {
				TuileAbbaye.add(this.tuileAdjacentes.get(0).tuileAdjacentes.get(3));
			}
		}	
		else if (this.tuileAdjacentes.get(3)!=null && this.tuileAdjacentes.get(3).tuileAdjacentes.get(0)!=null) { // else pour �viter de l'ajouter 2 fois si les 2 chemins sont valide, diagonale haut/gauche en passant par la gauche
			if (this.tuileAdjacentes.get(3).tuileAdjacentes.get(0).tabCarac[4]==Terrain.ABBAYE) {
				TuileAbbaye.add(this.tuileAdjacentes.get(3).tuileAdjacentes.get(0));
			}
		}
		
		if( this.tuileAdjacentes.get(0)!=null && this.tuileAdjacentes.get(0).tuileAdjacentes.get(1)!=null){ // diagonale haut/droite en passant par le haut
			if (this.tuileAdjacentes.get(0).tuileAdjacentes.get(1).tabCarac[4]==Terrain.ABBAYE) {
				TuileAbbaye.add(this.tuileAdjacentes.get(0).tuileAdjacentes.get(1));
			}
		}	
		else if (this.tuileAdjacentes.get(1)!=null && this.tuileAdjacentes.get(1).tuileAdjacentes.get(0)!=null) { // diagonale haut/droite en passant par la droite
			if (this.tuileAdjacentes.get(1).tuileAdjacentes.get(0).tabCarac[4]==Terrain.ABBAYE) {
				TuileAbbaye.add(this.tuileAdjacentes.get(1).tuileAdjacentes.get(0));
			}
		}
		
		if( this.tuileAdjacentes.get(2)!=null && this.tuileAdjacentes.get(2).tuileAdjacentes.get(1)!=null){ // diagonale bas/droite en passant par le bas
			if (this.tuileAdjacentes.get(2).tuileAdjacentes.get(1).tabCarac[4]==Terrain.ABBAYE) {
				TuileAbbaye.add(this.tuileAdjacentes.get(2).tuileAdjacentes.get(1));
			}
		}	
		else if (this.tuileAdjacentes.get(1)!=null && this.tuileAdjacentes.get(1).tuileAdjacentes.get(2)!=null) { // diagonale bas/droite en passant par la droite
			if (this.tuileAdjacentes.get(1).tuileAdjacentes.get(2).tabCarac[4]==Terrain.ABBAYE) {
				TuileAbbaye.add(this.tuileAdjacentes.get(1).tuileAdjacentes.get(2));
			}
		}
		
		if( this.tuileAdjacentes.get(2)!=null && this.tuileAdjacentes.get(2).tuileAdjacentes.get(3)!=null){ // diagonale bas/gauche en passant par le bas
			if (this.tuileAdjacentes.get(2).tuileAdjacentes.get(3).tabCarac[4]==Terrain.ABBAYE) {
				TuileAbbaye.add(this.tuileAdjacentes.get(2).tuileAdjacentes.get(3));
			}
		}	
		else if (this.tuileAdjacentes.get(3)!=null && this.tuileAdjacentes.get(3).tuileAdjacentes.get(2)!=null) { // diagonale bas/gauche en passant par la gauche
			if (this.tuileAdjacentes.get(3).tuileAdjacentes.get(2).tabCarac[4]==Terrain.ABBAYE) {
				TuileAbbaye.add(this.tuileAdjacentes.get(3).tuileAdjacentes.get(2));
			}
		}
		
		return TuileAbbaye;
				
	}
	
	
	public boolean verifAbbayeComplete (){
		// v�rifie si une abbaye est compl�te
		if ( this.tuileAdjacentes.get(0)!=null && this.tuileAdjacentes.get(1)!=null && this.tuileAdjacentes.get(2)!=null && this.tuileAdjacentes.get(3)!=null ) { // les 4 adjacentes
				return (this.tuileAdjacentes.get(0).tuileAdjacentes.get(3)!=null && this.tuileAdjacentes.get(0).tuileAdjacentes.get(1)!=null && // les 2 diagonales du haut
						this.tuileAdjacentes.get(2).tuileAdjacentes.get(3)!=null && this.tuileAdjacentes.get(2).tuileAdjacentes.get(1)!=null ); // les 2 diagonales du bas
		}
		else return false;
	}
	
	
	
	
	
	
	
	
	
}
 
