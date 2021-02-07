package dr.manhattan.external.scripts.mchopper;

import dr.manhattan.external.api.MScript;
import dr.manhattan.external.api.interact.MInteract;
import dr.manhattan.external.api.objects.MObjects;
import dr.manhattan.external.api.player.MInventory;
import dr.manhattan.external.api.player.MPlayer;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;
import org.pf4j.Extension;

import javax.inject.Inject;

@Extension
@PluginDescriptor(
        name = "MChopper",
        enabledByDefault = false,
        description = "Cut all the wood",
        tags = {"OpenOSRS", "ProjectM", "Woodcutting", "Automation"},
        type = PluginType.SKILLING
)
public class MChopper extends MScript {

    @Inject
    private Client client;

    @Override
    public int loop() {
        if (MPlayer.isIdle()) {
            if (!MInventory.isFull()) {
                chopTrees();
            } else {
                dropTrees();
            }
        }
        return 1000;
    }



    private void chopTrees() {
        GameObject tree = new MObjects()
                .hasName("Tree", "Dead tree", "Oak", "Willow")
                .hasAction("Chop down")
                .isWithinDistance(MPlayer.location(), 20 )
                .result()
                .nearestTo(MPlayer.get());
        if (tree != null) {
            log.info( tree.getId() + " at " + tree.getWorldLocation().toString());
            MInteract.GameObject(tree, ACTIONS);
        }
    }

    private void dropTrees() {
        MInventory.dropAll("Logs", "Oak logs", "Willow logs");
    }
}