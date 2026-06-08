package com.blzd.fly;

import com.griefdefender.api.GriefDefender;
import com.griefdefender.api.claim.Claim;
import org.bukkit.entity.Player;
import com.griefdefender.api.permission.flag.FlagPermission;
import com.griefdefender.api.permission.TrustTypes;
import com.griefdefender.api.permission.*;

public class ClaimManager {

    public boolean isInClaim(Player player) {

        Claim claim = getClaim(player);

        return claim != null && !claim.isWilderness();
    }

    public Claim getClaim(Player player) {

        return GriefDefender.getCore()
                .getClaimAt(player.getLocation());
    }

    public boolean isClaimOwner(Player player) {

        Claim claim = getClaim(player);

        if (claim == null || claim.isWilderness()) {
            return false;
        }

        return player.getUniqueId().equals(claim.getOwnerUniqueId());
    }

    public boolean canUseClaimFlight(Player player) {

        Claim claim = getClaim(player);
    
        if (claim == null || claim.isWilderness()) {
            return false;
        }
    
        if (player.getUniqueId().equals(claim.getOwnerUniqueId())) {
            return true;
        }
    
        return claim.isUserTrusted(
                player.getUniqueId(),
                null
        );
    }
}
