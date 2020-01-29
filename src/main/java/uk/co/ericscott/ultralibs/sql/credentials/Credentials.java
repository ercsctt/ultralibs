package uk.co.ericscott.ultralibs.sql.credentials;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Credentials {

    private String host;
    private int port;

    private String username;
    private String password;

    private String database;

}