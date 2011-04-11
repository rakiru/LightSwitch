package me.br_.bukkit.LightSwitch;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;

public class LightSwitchBlockListener extends BlockListener {
	private List<Location> levers;

	public LightSwitchBlockListener() {
		levers = new ArrayList<Location>();
	}

	public void save() {
	}

	@Override
	public void onBlockRedstoneChange(BlockRedstoneEvent event) {
		Block block = event.getBlock();
		World world = event.getBlock().getWorld();
		if (levers.contains(block.getLocation())) {
			if (world.getTime() > 13000) {
				world.setTime(0);
				for (Location l : levers) {
					Block lever = l.getBlock();
					lever.setData((byte) (lever.getData() % 8));
				}
			} else {
				world.setTime(13000);
				for (Location l : levers) {
					Block lever = l.getBlock();
					lever.setData((byte) (8 + (lever.getData() % 8)));
				}
			}
		} else if (block.getType() == Material.LEVER) {
			BlockFace[] faces = { BlockFace.NORTH, BlockFace.SOUTH,
					BlockFace.EAST, BlockFace.WEST, BlockFace.DOWN,
					BlockFace.DOWN };
			for (int i = 1; i <= 6; i++) {
				if (block.getFace(faces[i - 1]).getType() == Material.DIAMOND_BLOCK
						&& (block.getData() % 8) == i) {
					levers.add(block.getLocation());
					onBlockRedstoneChange(new BlockRedstoneEvent(block, 1, 0));
				}
			}
		}
	}

	@Override
	public void onBlockPlace(BlockPlaceEvent event) {
		Block block = event.getBlock();
		if (block.getType() != Material.LEVER
				|| event.getBlockAgainst().getType() != Material.DIAMOND_BLOCK) {
			return;
		}
		if (block.getWorld().getTime() > 13000) {
			block.setData((byte) (8 + (block.getData() % 8)));
		} else {
			block.setData((byte) (block.getData() % 8));
		}
		levers.add(block.getLocation());
		event.getPlayer().sendMessage("Light Switch created.");
	}

	@Override
	public void onBlockBreak(BlockBreakEvent event) {
		Block block = event.getBlock();
		if (levers.remove(block.getLocation())) {
			event.getPlayer().sendMessage("Light Switch destroyed.");
		} else if (block.getType() == Material.DIAMOND_BLOCK) {
			BlockFace[] faces = { BlockFace.NORTH, BlockFace.WEST,
					BlockFace.EAST, BlockFace.SOUTH, BlockFace.UP };
			int broken = 0;
			for (BlockFace face : faces) {
				if (levers.remove(block.getFace(face).getLocation())) {
					broken++;
				}
			}
			if (broken > 1) {
				event.getPlayer().sendMessage(
						String.format("%d Light Switches destroyed.", broken));
			} else if (broken == 1) {
				event.getPlayer().sendMessage("Light Switch destroyed.");
			}
		}
	}
}
