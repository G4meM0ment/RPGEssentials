package me.G4meM0ment.GSkills;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.herocraftonline.heroes.Heroes;
import com.herocraftonline.heroes.api.SkillResult;
import com.herocraftonline.heroes.characters.Hero;
import com.herocraftonline.heroes.characters.skill.ActiveSkill;
import com.herocraftonline.heroes.characters.skill.SkillConfigManager;
import com.herocraftonline.heroes.characters.skill.SkillSetting;
import com.herocraftonline.heroes.characters.skill.SkillType;

public class SkillPandemic extends ActiveSkill {

	public SkillPandemic(Heroes plugin) 
    {
        super(plugin, "Pandemic");
        setUsage("/skill pandemic");
        setDescription("You cast a mighty aura draining your enemies life and heals you");
        setTypes(SkillType.HEAL);
        setTypes(SkillType.HARMFUL);
        setArgumentRange(0, 0);
        setIdentifiers("skill pandemic");
    }

    @Override
    public String getDescription(Hero hero) 
    {
    	long duration = SkillConfigManager.getUseSetting(hero, this, SkillSetting.DURATION.node(), 5000, false)
    			+SkillConfigManager.getUseSetting(hero, this, SkillSetting.DURATION_INCREASE.node(), 1000, false);
    	long period = SkillConfigManager.getUseSetting(hero, this, SkillSetting.PERIOD.node(), 3000, false);
    	final double damage = SkillConfigManager.getUseSetting(hero, this, SkillSetting.DAMAGE.node(), 1.0, false)
    			+SkillConfigManager.getUseSetting(hero, this, SkillSetting.DAMAGE_INCREASE.node(), 0.2, false);
    	final int radius = SkillConfigManager.getUseSetting(hero, this, SkillSetting.RADIUS.node(), 6, false)
    			+SkillConfigManager.getUseSetting(hero, this, SkillSetting.RADIUS_INCREASE.node(), 0, false);
    	final int targets = SkillConfigManager.getUseSetting(hero, this, "targets", 6, false);
    	final int mana = SkillConfigManager.getUseSetting(hero, this, SkillSetting.MANA.node(), 40, false);
    	
        String description = getDescription().replace("$1", duration+"").replace("$2", period+"").replace("§3", damage+"").replace("§4", radius+"").replace("§5", targets+"").replace("§6", mana+"");
        return description;
    }

    @Override
    public ConfigurationSection getDefaultConfig() 
    {
        ConfigurationSection node = super.getDefaultConfig();
        node.set(SkillSetting.DURATION.node(), 5000);
        node.set(SkillSetting.DURATION_INCREASE.node(), 1000);
        node.set(SkillSetting.PERIOD.node(), 3000);
        node.set(SkillSetting.DAMAGE.node(), 1.0);
        node.set(SkillSetting.DAMAGE_INCREASE.node(), 0.2);
        node.set(SkillSetting.RADIUS.node(), 6);
        node.set(SkillSetting.RADIUS_INCREASE.node(), 0);
    	node.set(SkillSetting.MANA.node(), 40);
        node.set("targets", 6);
        return node;
    }
    
	@Override
    public SkillResult use(final Hero hero, String args[]) 
	{
    	final Player p = hero.getPlayer();
    	if(!p.getItemInHand().getType().equals(Material.ENCHANTED_BOOK))
    	{
    		p.sendMessage(ChatColor.GRAY+"Du musst ein Magierbuch tragen um disen Zauber zu wirken!");
    		return SkillResult.CANCELLED;
    	}

    	final SkillPandemic skill = this;
    	final long duration = SkillConfigManager.getUseSetting(hero, this, SkillSetting.DURATION.node(), 5000, false)
    			+SkillConfigManager.getUseSetting(hero, this, SkillSetting.DURATION_INCREASE.node(), 1000, false);
    	final long period = SkillConfigManager.getUseSetting(hero, this, SkillSetting.PERIOD.node(), 3000, false);
    	final double damage = SkillConfigManager.getUseSetting(hero, this, SkillSetting.DAMAGE.node(), 1.0, false)
    			+SkillConfigManager.getUseSetting(hero, this, SkillSetting.DAMAGE_INCREASE.node(), 0.2, false);
    	final int radius = SkillConfigManager.getUseSetting(hero, this, SkillSetting.RADIUS.node(), 6, false)
    			+SkillConfigManager.getUseSetting(hero, this, SkillSetting.RADIUS_INCREASE.node(), 0, false);
    	final int targets = SkillConfigManager.getUseSetting(hero, this, "targets", 6, false);
    	final int mana = SkillConfigManager.getUseSetting(hero, this, SkillSetting.MANA.node(), 40, false)/((int) (duration/period));
    	
    	p.sendMessage(ChatColor.GRAY+"Debug: "+duration+", "+period+", "+damage+", "+radius+", "+targets+", "+mana);
    	
    	final int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable()
    	{
			@Override
			public void run() 
			{
				if(hero.getMana() >= mana)
				{
					p.getWorld().playEffect(p.getLocation().getBlock().getRelative(radius, 0, 0).getLocation(), Effect.ENDER_SIGNAL, 10);
					p.getWorld().playEffect(p.getLocation().getBlock().getRelative(radius, 0, 0).getLocation(), Effect.SMOKE, 10);
					p.getWorld().playEffect(p.getLocation().getBlock().getRelative(-radius, 0, 0).getLocation(), Effect.ENDER_SIGNAL, 10);
					p.getWorld().playEffect(p.getLocation().getBlock().getRelative(-radius, 0, 0).getLocation(), Effect.SMOKE, 10);
					p.getWorld().playEffect(p.getLocation().getBlock().getRelative(0, 0, radius).getLocation(), Effect.ENDER_SIGNAL, 10);
					p.getWorld().playEffect(p.getLocation().getBlock().getRelative(0, 0, radius).getLocation(), Effect.SMOKE, 10);
					p.getWorld().playEffect(p.getLocation().getBlock().getRelative(0, 0, -radius).getLocation(), Effect.ENDER_SIGNAL, 10);
					p.getWorld().playEffect(p.getLocation().getBlock().getRelative(0, 0, -radius).getLocation(), Effect.SMOKE, 10);
					p.getWorld().playSound(p.getLocation(), Sound.FUSE, 40, 1);
					
					int counter = 0;
					for(Entity e : hero.getPlayer().getNearbyEntities(radius, radius, radius))
					{
						if(targets <= counter) break;
						if(!(e instanceof LivingEntity)) return;
						LivingEntity le = (LivingEntity) e;
						if(SkillPandemic.damageCheck(p, le))
						{
							skill.damageEntity(le, p, damage);
							hero.heal(damage);
							counter++;
						}
					}
					hero.setMana(hero.getMana()-mana);
				}
			}
    	}, 0, period/50);
    	Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
    	{
			@Override
			public void run() 
			{
				Bukkit.getScheduler().cancelTask(id);
			}
    	}, duration/50);
    	
        return SkillResult.NORMAL;
    }
}
