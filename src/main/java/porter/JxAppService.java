package porter;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import porter.util.configloader.JiConfigurtionLoader;
import porter.util.configloader.JxXmlConfigurtionLoader;
import porter.verticle.JxVerticleDeclaration;

import java.util.List;

/**
 * Created by parker on 2015/10/14.
 */
public class JxAppService {

    private Vertx _vertx;
    private List<JxVerticleDeclaration> _verticleDeclerations;

    public JxAppService() {
        // Create a vertx instance. All verticles in this project is run on this vertx instance.
        _vertx = Vertx.vertx();
    }

    public void loadConfiguration() {
        // Read deployment configuration.
        JiConfigurtionLoader loader = new JxXmlConfigurtionLoader("/porter/config.xml");
        _verticleDeclerations = loader.getVerticleDeclarations();
    }

    public void deployVerticles() {
        // Create each verticles corresponding to list and deploy them.
        for (JxVerticleDeclaration verticleDefine : _verticleDeclerations) {
            DeploymentOptions options = new DeploymentOptions();
            if (verticleDefine.getConfiguration() != null) {
                options.setConfig(verticleDefine.getConfiguration());
            }
            if(verticleDefine.getNumber() != null){
                options.setInstances(verticleDefine.getNumber());
            }
            _vertx.deployVerticle(verticleDefine.getVerticleClassName(), options);
        }
    }
}
