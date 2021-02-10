package dr.manhattan.external.scripts.mminer;

import dr.manhattan.external.api.MScript;
import dr.manhattan.external.api.interact.MInteract;
import dr.manhattan.external.api.items.MInventory;
import dr.manhattan.external.api.objects.MObjects;
import dr.manhattan.external.api.player.MPlayer;
import net.runelite.api.GameObject;
import net.runelite.api.Skill;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;
import org.pf4j.Extension;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Extension
@PluginDescriptor(
        name = "MMiner",
        enabledByDefault = false,
        description = "Mine the rocks",
        tags = {"OpenOSRS", "ProjectM", "Woodcutting", "Automation"},
        type = PluginType.SKILLING
)
public class MMiner extends MScript {
    private static final Object BUS_OBJ = new Object();
    final String[] ACTIONS = {"Mine"};
    final String[] ORES = {"Copper ore", "Tin ore", "Iron ore", "Clay"};

    @Inject
    private EventBus eventBus;

    @Override
    public int loop() {
        if (MPlayer.isIdle() || ignoreIdle) {
            if (!MInventory.isFull()) {
                mineRocks();
            } else {
                dropOres();
            }
        }
        return 1000;
    }

    private void dropOres() {
        MInventory.dropAll(ORES);
    }

    private WorldPoint rockPos = null;
    private boolean ignoreIdle = false;

    private void mineRocks() {

        List<List<Integer>> rockIds = new ArrayList<>();
        int miningLevel = MPlayer.getLevel(Skill.MINING);
        if(miningLevel >= 15){
            rockIds.add(List.of(11364, 11365));
        }
        rockIds.add(List.of(1161, 10943, 11360, 11361, 11362, 11363));


        for(List<Integer> ids: rockIds){
            GameObject rock = new MObjects()
                    .idEquals(ids)
                    .hasAction(ACTIONS)
                    .result()
                    .nearestTo(MPlayer.get());
            if (rock != null) {
                log.info( rock.getId() + " at " + rock.getWorldLocation().toString());
                rockPos = rock.getWorldLocation();
                MInteract.gameObject(rock, ACTIONS);
                ignoreIdle = false;
                return;
            }
        }
    }

    @Override
    protected void startUp() throws Exception {
        eventBus.subscribe(GameObjectDespawned.class, BUS_OBJ, this::objectDespawned);
        super.startUp();
    }
    private void objectDespawned(GameObjectDespawned event){
        if(event.getGameObject().getWorldLocation().equals(rockPos)){
            ignoreIdle = true;
        }
    }

    @Override
    protected void shutDown() throws Exception {
        eventBus.unregister(BUS_OBJ);
        super.shutDown();
    }
}