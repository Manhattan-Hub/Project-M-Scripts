package dr.manhattan.external.scripts.mchopper;

import dr.manhattan.external.api.MScript;
import dr.manhattan.external.api.interact.MInteract;
import dr.manhattan.external.api.items.MInventory;
import dr.manhattan.external.api.objects.MObjects;
import dr.manhattan.external.api.player.MPlayer;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.Skill;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;
import org.pf4j.Extension;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

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

        List<List<String>> treeList = new ArrayList<>();
        int wcLevel = MPlayer.getLevel(Skill.WOODCUTTING);
        if(wcLevel >= 30){
            treeList.add(List.of("Willow"));
        }
        if(wcLevel >= 15){
            treeList.add(List.of("Oak"));
        }
        treeList.add(List.of("Tree", "Dead tree"));

        for(List<String> trees: treeList){
            GameObject tree = new MObjects()
                    .hasName(trees)
                    .hasAction("Chop down")
                    .starNearest();

            if (tree != null) {
                log.info( tree.getId() + " at " + tree.getWorldLocation().toString());
                MInteract.gameObject(tree, "Chop down");
                return;
            }
        }

    }

    private void dropTrees() {
        MInventory.dropAllUnnoted("Logs", "Oak logs", "Willow logs");
    }
}