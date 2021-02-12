package dr.manhattan.external.scripts.mchickenkiller;

import dr.manhattan.external.api.MScript;
import dr.manhattan.external.api.calc.MCalc;
import dr.manhattan.external.api.interact.MInteract;
import dr.manhattan.external.api.items.MInventory;
import dr.manhattan.external.api.npcs.MNpcs;
import dr.manhattan.external.api.player.MPlayer;
import net.runelite.api.Actor;
import net.runelite.api.NPC;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;
import org.pf4j.Extension;

import java.util.ArrayList;
import java.util.List;

@Extension
@PluginDescriptor(
        name = "MChickenKiller",
        enabledByDefault = false,
        description = "Kill chickun, geat fethur",
        tags = {"OpenOSRS", "ProjectM", "Chicken", "Automation"},
        type = PluginType.SKILLING
)
public class MChickenKiller extends MScript {

    @Override
    public int loop() {
        Actor interacting = MPlayer.get().getInteracting();
        if (MPlayer.isIdle() && (interacting == null || interacting.getHealthRatio() == -1)) {
            kill();
        }

        return MCalc.nextInt(300, 600);
    }

    private void kill() {
        log.info("Kill");
        List<List<String>> names = new ArrayList<>();
        String[] action = {"Attack"};


        NPC target = new MNpcs()
                .nameEquals("Chicken", "Seagull")
                .hasAction(action)
                .notInteracting()
                .notDead()
                .isReachable()
                .starNearest();

        if (target != null) {
            log.info(target.getId() + " at " + target.getWorldLocation().toString());
            MInteract.npc(target, action);
            return;
        } else log.info("Cant find anything to murder");

    }
}