package com.blzd.fly;

import com.griefdefender.api.GriefDefender;
import com.griefdefender.api.claim.Claim;
import com.griefdefender.api.claim.TrustTypes;
import org.bukkit.entity.Player;

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
    
        // Claim owner
        if (player.getUniqueId().equals(claim.getOwnerUniqueId())) {
            return true;
        }
    
        // Builder trust
        if (claim.isUserTrusted(player.getUniqueId(), TrustTypes.MANAGER)) {
            return true;
        }
        
        return claim.isUserTrusted(player.getUniqueId(), TrustTypes.BUILDER);
    }                                                                                                                                                                                      

    public boolean canUseClaimFlight(Player player) {
        return isClaimOwner(player);
    }
}
