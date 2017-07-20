package voxxrin2;

import com.google.common.base.Charsets;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import restx.config.ConfigLoader;
import restx.config.ConfigSupplier;
import restx.factory.Module;
import restx.factory.Provides;
import restx.security.CORSAuthorizer;
import restx.security.SignatureKey;
import restx.security.StdCORSAuthorizer;

@Module
public class AppModule {
    @Provides
    public SignatureKey signatureKey() {
        return new SignatureKey("voxxrin2 voxxrin2 ee144d01-614c-4bc7-a7c4-2a78f507a363 -7912789517386278224".getBytes(Charsets.UTF_8));
    }

    @Provides
    public ConfigSupplier appConfigSupplier(ConfigLoader configLoader) {
        // Load settings.properties in voxxrin2 package as a set of config entries
        return configLoader.fromResource("voxxrin2/settings");
    }

    @Provides(priority = -1000)
    public CORSAuthorizer CORSAuthorizer() {
        StdCORSAuthorizer.Builder builder = StdCORSAuthorizer.builder();
        return builder.setOriginMatcher(Predicates.<CharSequence>alwaysTrue())
                .setAllowedHeaders(ImmutableList.of("Authorization", "access-token", "Origin",
                        "X-Requested-With", "Content-Type", "Accept", "If-Modified-Since"))
                .setPathMatcher(Predicates.<CharSequence>alwaysTrue())
                .setAllowedMethods(ImmutableList.of("GET", "POST", "PUT", "OPTIONS", "DELETE"))
                .build();
    }
}
