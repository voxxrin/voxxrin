package voxxrin2.rest;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Iterables;
import restx.annotations.*;
import restx.factory.Component;
import restx.security.PermitAll;
import restx.security.RolesAllowed;
import voxxrin2.domain.Presentation;
import voxxrin2.domain.Subscriptions;
import voxxrin2.persistence.PresentationsDataService;

import java.util.List;

@Component
@RestxResource
public class PresentationsResource {

    private final PresentationsDataService presentationsDataService;

    public PresentationsResource(PresentationsDataService presentationsDataService) {
        this.presentationsDataService = presentationsDataService;
    }

    @GET("/presentations")
    @PermitAll
    @Produces("application/json;view=voxxrin2.serialization.Views$Presentations$List")
    public Iterable<Presentation> getPresentations() {
        return presentationsDataService.findAll();
    }

    @GET("/presentations/{id}")
    @PermitAll
    @Produces("application/json;view=voxxrin2.serialization.Views$Presentations$Details")
    public Optional<Presentation> getPresentation(String id) {
        return presentationsDataService.findById(id);
    }

    @POST("/presentations")
    @Consumes("application/json;view=voxxrin2.serialization.Views$Presentations$Details")
    @RolesAllowed({"ADM", "restx-admin"})
    public Presentation savePresentation(Presentation presentation) {
        return presentationsDataService.save(presentation);
    }

    @POST("/presentations/several")
    @Consumes("application/json;view=voxxrin2.serialization.Views$Presentations$Details")
    @RolesAllowed({"ADM", "restx-admin"})
    public List<Presentation> savePresentations(List<Presentation> presentations) {
        for (Presentation presentation : presentations) {
            presentationsDataService.save(presentation);
        }
        return presentations;
    }

    @GET("/events/{eventId}/presentations")
    @PermitAll
    @Produces("application/json;view=voxxrin2.serialization.Views$Presentations$List")
    public Iterable<Presentation> getEventPresentations(String eventId) {
        return presentationsDataService.findByEvent(eventId);
    }

    @GET("/days/{dayId}/presentations")
    @PermitAll
    @Produces("application/json;view=voxxrin2.serialization.Views$Presentations$List")
    public Iterable<Presentation> getDayPresentations(String dayId) {
        return presentationsDataService.findByDay(dayId);
    }

    @GET("/events/{eventId}/subscriptions")
    @PermitAll
    public Iterable<Subscriptions> getSubscriptions(@Param(kind = Param.Kind.PATH) String eventId) {

        Iterable<Presentation> presentations = presentationsDataService.findByEvent(eventId);

        return Iterables.transform(presentations, new Function<Presentation, Subscriptions>() {
            @Override
            public Subscriptions apply(Presentation input) {
                return new Subscriptions()
                        .setPresentationId(input.getKey())
                        .setFavoriteCount(input.getFavoriteCount())
                        .setRemindersCount(input.getRemindMeCount());
            }
        });
    }
}
