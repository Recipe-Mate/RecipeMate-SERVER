package org.chefcrew.recipe.dto.response;

import java.util.List;

public record GetReplicableFoodResponse(
        List<String> replacableFoodNames
){
}
