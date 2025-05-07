package org.chefcrew.recipe.dto.request;

public record GetReplicableFoodRequest(
        String menuName,
        String replaceFoodName
) {
}
