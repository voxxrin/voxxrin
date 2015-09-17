package voxxrin2.webservices;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.common.collect.ImmutableMap;
import com.samskivert.mustache.Template;
import org.slf4j.Logger;
import restx.annotations.POST;
import restx.annotations.Param;
import restx.annotations.RestxResource;
import restx.common.Mustaches;
import restx.factory.Component;
import voxxrin2.domain.technical.PushStatus;

import static org.slf4j.LoggerFactory.getLogger;

@Component
@RestxResource
public class Push {

    private static final String IONIC_PUSH_URL = "https://push.ionic.io/api/v1/push";

    private static final Logger logger = getLogger(Push.class);

    private final Template tmpl;

    private final String ionicAppId;
    private final String ionicAppPrivateKey;

    public Push(WebServicesSettings webServicesSettings) {
        this.tmpl = Mustaches.compile("pushPayload.mustache");
        this.ionicAppId = webServicesSettings.ionicAppId();
        this.ionicAppPrivateKey = webServicesSettings.ionicAppPrivateKey();
    }

    @POST("/push/send")
    public PushStatus sendMsg(@Param(kind = Param.Kind.QUERY) String msg, @Param(kind = Param.Kind.QUERY) String token) {

        String payload = tmpl.execute(
                ImmutableMap.of(
                        "token", token,
                        "msg", msg
                )
        );

        // See http://docs.ionic.io/docs/push-sending-push
        HttpRequest request = HttpRequest.post(IONIC_PUSH_URL)
                .header("X-Ionic-Application-Id", ionicAppId)
                .basic(ionicAppPrivateKey, "")
                .contentType("application/json")
                .send(payload);

        int code = request.code();
        String body = request.body();

        logger.info("{} : {}", code, body);

        return PushStatus.of(code, body);
    }

}