package dr.manhattan.external.scripts.mminer;

import dr.manhattan.external.api.MScript;
import dr.manhattan.external.api.interact.MInteract;
import dr.manhattan.external.api.objects.MObjects;
import dr.manhattan.external.api.player.MInventory;
import dr.manhattan.external.api.player.MPlayer;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;
import org.pf4j.Extension;

import javax.inject.Inject;

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
    final int[] IDS = {1161, 10943, 11360, 11361};
    final String[] ACTIONS = {"Mine"};
    final String[] ORES = {"Copper ore", "Tin ore"};
    @Inject
    private Client client;
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
        GameObject rock = new MObjects()
                .idEquals(IDS)
                .hasAction(ACTIONS)
                .isWithinDistance(MPlayer.location(), 20 )
                .result()
                .nearestTo(MPlayer.get());
        if (rock != null) {
            log.info( rock.getId() + " at " + rock.getWorldLocation().toString());
            rockPos = rock.getWorldLocation();
            MInteract.GameObject(rock, ACTIONS);
            ignoreIdle = false;
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