package stream.support.command.api.repositories;

import org.springframework.stereotype.Service;
import stream.support.command.api.network.HTTPRequests;

@Service
public class FollowageRepository {

    private final HTTPRequests requests;

    public FollowageRepository(HTTPRequests requests) {
        this.requests = requests;
    }

    public String getFollowageForUser(String channel, String user) {
        String followage = requests.getFollowage(channel, user);
        if (followage.contains("not follow")) {
            return user + " is not following " + channel + "!";
        } else if (followage.contains("not found")) {
            String effectedUser = followage.contains(user.toLowerCase().strip()) ? user : channel;
            return "No user with name: " + effectedUser + " found!";
        } else {
            return user + " is following " + channel + " for " + followage + "!";
        }
    }

}
