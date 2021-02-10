package dr.manhattan.external.scripts.mfisher;

import dr.manhattan.external.api.MScript;
import dr.manhattan.external.api.calc.MCalc;
import dr.manhattan.external.api.interact.MInteract;
import dr.manhattan.external.api.items.MInventory;
import dr.manhattan.external.api.npcs.MNpcs;
import dr.manhattan.external.api.player.MPlayer;
import net.runelite.api.NPC;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;
import org.pf4j.Extension;

import java.util.ArrayList;
import java.util.List;

@Extension
@PluginDescriptor(
        name = "MFisher",
        enabledByDefault = false,
        description = "Fish the fish",
        tags = {"OpenOSRS", "ProjectM", "Fishing", "Automation"},
        type = PluginType.SKILLING
)
public class MFisher extends MScript {

    @Override
    public int loop() {
        if (MPlayer.isIdle()) {
            if (!MInventory.isFull()) {
                fish();
            } else {
                drop();
            }
        }
        return MCalc.nextInt(300, 10000);
    }

    private void drop() {
        MInventory.dropAll("Raw shrimps", "Raw anchovies");
    }

    private void fish() {

        List<List<Integer>> spotIds = new ArrayList<>();
        spotIds.add(List.of(1530));

        for(List<Integer> ids: spotIds){
            NPC spot = new MNpcs()
                    .isID(ids)
                    .hasAction("Net")
                    .result()
                    .nearestTo(MPlayer.get());
            if (spot != null) {
                log.info( spot.getId() + " at " + spot.getWorldLocation().toString());
                MInteract.npc(spot, "Net");
                return;
            }
            else log.info("Cant find fishing spot");
        }
    }
}