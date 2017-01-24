package bacnet;

public class Launcher {
	
	public static void main(String[] args) {
		Connexion cn = new Connexion();
		System.out.println(cn.getValue());
		cn.disconnection();
	}

}
