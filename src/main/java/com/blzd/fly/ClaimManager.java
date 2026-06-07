package com.blzd.fly;

import com.griefdefender.api.GriefDefender;
import com.griefdefender.api.claim.Claim;
import org.bukkit.entity.Player;

public class ClaimManager {

    public boolean isInClaim(Player player) {

        Claim claim = GriefDefender.getCore()
                .getClaimAt(player.getLocation());

        return claim != null && !claim.isWilderness();
    }
}
