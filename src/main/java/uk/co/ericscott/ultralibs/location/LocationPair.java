package uk.co.ericscott.ultralibs.location;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class LocationPair
{
    private LazyLocation loc1, loc2;
}
