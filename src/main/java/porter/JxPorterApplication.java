package porter;

/**
 * Define main function of this project Run as socket server, used to
 * communicate between sensor client and accepter client.
 */
public class JxPorterApplication {

	public static void main(String[] args) {
        System.out.println("Application started.");
        JxAppService svc = new JxAppService();

        svc.loadConfiguration();
        svc.deployVerticles();
        System.out.println("Start listening...");
    }
}
