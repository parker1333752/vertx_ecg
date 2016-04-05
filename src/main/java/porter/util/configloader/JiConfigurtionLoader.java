package porter.util.configloader;

import porter.verticle.JxVerticleDeclaration;

import java.util.List;

/**
 * Created by parker on 2015/10/14.
 */
public interface JiConfigurtionLoader {
    /**
     * Read verticle declaration informations.
     * @return
     */
    List<JxVerticleDeclaration> getVerticleDeclarations();
}
