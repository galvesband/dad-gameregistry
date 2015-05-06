package es.us.dad.gameregistry.server.controller

import com.darylteo.vertx.promises.groovy.Promise
import es.us.dad.gameregistry.server.domain.GameSession
import es.us.dad.gameregistry.server.exception.MethodNotAllowedException
import es.us.dad.gameregistry.server.service.ILoginService
import es.us.dad.gameregistry.server.service.LoginServiceMock
import es.us.dad.gameregistry.server.service.SessionService
import es.us.dad.gameregistry.server.util.Authenticated
import es.us.dad.gameregistry.server.util.DELETE
import es.us.dad.gameregistry.server.util.GET
import es.us.dad.gameregistry.server.util.POST
import es.us.dad.gameregistry.server.util.PUT
import io.netty.handler.codec.http.HttpResponseStatus
import org.vertx.groovy.core.http.HttpServerRequest

class SessionController extends Controller {

    private final SessionService sessionService

    public SessionController(ILoginService loginService, SessionService sessionService) {
        super(loginService)
        this.sessionService = sessionService
    }

    @Authenticated
    @GET("/sessions/:id")
    public void getSession(HttpServerRequest request) {
        UUID id = UUID.fromString(request.params.get("id"))

        sessionService.getSession(id).then({ GameSession session ->
            sendJsonResponse(request, session)
        }, { Exception ex ->
            sendErrorResponse(request, ex)
        })
    }

    @Authenticated
    @POST("/sessions/:id")
    public void postSession(HttpServerRequest request) {
        sendErrorResponse(request, new MethodNotAllowedException())
    }

    @Authenticated
    @PUT("/sessions/:id")
    public void changeSession(HttpServerRequest request) {
        UUID id = UUID.fromString(request.params.get("id"))

        sessionService.finishSession(id).then({ GameSession session ->
            sendJsonResponse(request, session)
        }, { Exception ex ->
            sendErrorResponse(request, ex)
        })
    }

    @Authenticated
    @DELETE("/sessions/:id")
    public void deleteSession(HttpServerRequest request) {
        UUID id = UUID.fromString(request.params.get("id"))

        sessionService.deleteSession(id).then({
            sendJsonResponse(request, [:], HttpResponseStatus.NO_CONTENT)
        }, { Exception ex ->
            sendErrorResponse(request, ex)
        })
    }

}
