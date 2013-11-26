import java.util.ArrayList;
import java.util.Arrays;


public class Tuile {
	private Terrain [] tabCarac; // 0 nord, 1 est, 2 sud, 3 ouest, 4 centre
	private boolean [] tabPresenceChamps; // 0 NNO, 1 NNE, 2 ENE, 3 ESE, 4 SSE, 5 SSO, 6 OSO, 7 ONO
	private boolean [][] tabConnexit�Bordure;
	private boolean [][] tabConnexit�Champs;
	private int bouclier; // 0 nord, 1 est, 2 sud, 3 ouest, 4 centre, 5 pas de bouclier.
	private Pion PionPlac�;
	private ArrayList<Tuile> tuileAdjacentes;
	private int sensTuile;
	private int x; // abscisse de la tuile dans le rep�re du jeu ( ensemble des tuile pos�e )
	private int y; // ordonn�e ...
	
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
		this.tuileAdjacentes=new ArrayList<Tuile>(4);
		
	}
	
	public Tuile (){
		this.tabCarac= new Terrain[4];
		for(int i =0;i<4;i++){
		this.tabCarac[i]=Terrain.VIDE;
		}
		this.tabPresenceChamps= new boolean [1];
		this.tabConnexit�Bordure= new boolean [1][1];
		this.tabConnexit�Champs=new boolean [1][1];
		this.bouclier=5;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tuile other = (Tuile) obj;
		if (bouclier != other.bouclier)
			return false;
		if (sensTuile != other.sensTuile)
			return false;
		if (!Arrays.equals(tabCarac, other.tabCarac))
			return false;
		if (!Arrays.equals(tabConnexit�Bordure, other.tabConnexit�Bordure))
			return false;
		if (!Arrays.equals(tabConnexit�Champs, other.tabConnexit�Champs))
			return false;
		if (!Arrays.equals(tabPresenceChamps, other.tabPresenceChamps))
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	public void retirePion(){
		this.PionPlac�=null;
	}
	
	public int getX(){
		return this.x;
	}
	
	public int getY(){
		return this.y;
	}
	
	public Terrain getCarac(int i){
		return this.tabCarac[i];
	}
	
	public int getBouclier(){
		return this.bouclier;
	}
	
	public boolean [][] getConnexit�Bordure(){
		return this.tabConnexit�Bordure;
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
	
	/*public boolean verifPoseTuileLegale (Plateau p, ArrayList<Tuile> adjacente, int sens){
		// TODO gestion de si la tuile est pos�e sur une tuile d�j� existante.
		// dans l'attribut ArrayList<Tuile> tuileAdjacentes, l'ordre correspond � leur place par rapport � la tuile qu'on veut poser : 0 = dessus, 1 = droite, 2 = dessous, 3 = � gauche
		if (adjacente.get(0) == null && adjacente.get(1) == null && adjacente.get(2) == null && adjacente.get(3) == null) {
			// On ne peut pas pose une tuile si elle n'est pas entour�e d'au moins une tuile.
			return false;
		}
		else if(   (adjacente.get(0) != null && this.tabCarac[0]==adjacente.get(0).tabCarac[2])
				&& (adjacente.get(1) != null && this.tabCarac[1]==adjacente.get(1).tabCarac[3])
				&& (adjacente.get(2) != null && this.tabCarac[2]==adjacente.get(2).tabCarac[0]) 
				&& (adjacente.get(3) != null && this.tabCarac[3]==adjacente.get(3).tabCarac[1])) {
			// On v�rifie la compatibilit� des caract�ristiques entre la tuile propos�e et ses voisins.
			// Pour chaque voisin, on v�rifie qu'il existe, et seulement ensuite, on v�rifie la correspondance des caracs.
			return true;
		} else {
			// On retourne faux si les caracs des voisis ne correspondent pas.
			return false;
		}
	}*/
	
	public boolean verifPoseTuileLegale (Plateau p, int x, int y){
		if(p.isEmpty(x,y)){ // V�rifie que l'emplacement est disponible
			if(p.isEmpty(x-1, y) || p.getTuile(x-1, y).getCarac(1)==this.tabCarac[3]){ // V�rifie la compatibilit� avec la tuile de gauche si elle existe
				if(p.isEmpty(x, y+1) || p.getTuile(x, y+1).getCarac(2)==this.tabCarac[0]){ // V�rifie la compatibilit� avec la tuile du haut si elle existe
					if(p.isEmpty(x+1, y) || p.getTuile(x+1, y).getCarac(3)==this.tabCarac[1]){ // V�rifie la compatibilit� avec la tuile de droite si elle existe
						if(p.isEmpty(x, y-1) || p.getTuile(x, y-1).getCarac(0)==this.tabCarac[2]){ // V�rifie la compatibilit� avec la tuile du bas si elle existe
							if(!p.isEmpty(x-1, y) || !p.isEmpty(x, y+1) || !p.isEmpty(x+1, y) || !p.isEmpty(x, y-1) ){ // V�rifie si il y a au moins une tuile adjacentes
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	public void poseTuile (Plateau r, int x, int y){
		r.poseTuile(this, x, y);
		this.x = x;
		this.y = y;
	}

	/*public void poseTuile (ArrayList<Tuile> adjacente, Plateau r, int x, int y){
		// pr�-requis : la pose de la tuile est l�gale
		r.poseTuile(this, x, y);
		this.x = x;
		this.y = y;
		
		// Pour chaque voisin, a condition qu'il existe, on ajoute � cette tuile son nouveau voisin, et au voisin cette tuile.
		if (adjacente.get(0) != null) {
			this.tuileAdjacentes.add(0,adjacente.get(0));
			this.tuileAdjacentes.get(0).tuileAdjacentes.add(2,this);
		} 
		
		if (adjacente.get(1) != null) {
			this.tuileAdjacentes.add(1,adjacente.get(1));
			this.tuileAdjacentes.get(1).tuileAdjacentes.add(3,this);
		}
		
		if (adjacente.get(2) != null) {
			this.tuileAdjacentes.add(2,adjacente.get(2));
			this.tuileAdjacentes.get(2).tuileAdjacentes.add(0,this);
		}
		
		if (adjacente.get(3) != null) {
			this.tuileAdjacentes.add(3,adjacente.get(3));
			this.tuileAdjacentes.get(3).tuileAdjacentes.add(1,this);
		}
	}*/
	
	
	
	
}// fin de classe
 
