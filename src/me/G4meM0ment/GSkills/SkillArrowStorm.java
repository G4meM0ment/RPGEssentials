package me.G4meM0ment.GSkills;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.herocraftonline.heroes.Heroes;
import com.herocraftonline.heroes.api.SkillResult;
import com.herocraftonline.heroes.api.events.WeaponDamageEvent;
import com.herocraftonline.heroes.characters.Hero;
import com.herocraftonline.heroes.characters.skill.ActiveSkill;
import com.herocraftonline.heroes.characters.skill.SkillConfigManager;
import com.herocraftonline.heroes.characters.skill.SkillSetting;
import com.herocraftonline.heroes.characters.skill.SkillType;

public class SkillArrowStorm extends ActiveSkill {
    
	public List<Arrow> shotArrows = new ArrayList<Arrow>();
	
	public SkillArrowStorm(Heroes plugin) 
    {
        super(plugin, "ArrowStorm");
        setUsage("/skill arrowstorm");
        setDescription("You shoot a salve of arrows in a short period");
        setTypes(SkillType.DAMAGING);
        setArgumentRange(0, 0);
        setIdentifiers("skill arrowstorm");
        Bukkit.getServer().getPluginManager().registerEvents(new SkillHeroListener(this), plugin);
    }

    @Override
    public String getDescription(Hero hero) 
    {
    	
//    	double velocity = ((SkillConfigManager.getUseSetting(hero, this, "velocity", 1.0, false)));
    	double damage = ((SkillConfigManager.getUseSetting(hero, this, SkillSetting.DAMAGE.node(), 4.0, false)));
    	double damageLevel = ((SkillConfigManager.getUseSetting(hero, this, SkillSetting.DAMAGE_INCREASE.node(), 0.0, false)));
    	int arrows = ((SkillConfigManager.getUseSetting(hero, this, "arrows", 8, false)));
    	int arrowsLevel = ((SkillConfigManager.getUseSetting(hero, this, "arrows-per-level", 0, false)));
    	long period = ((SkillConfigManager.getUseSetting(hero, this, "period", 750, false)));
    	
        String description = getDescription().replace("$1", (damage+(damageLevel*hero.getLevel())) + "").replace("$2", (arrows+(arrowsLevel*hero.getLevel())) + "").replace("$3", period + "");
        return description;
    }

    @Override
    public ConfigurationSection getDefaultConfig() 
    {
        ConfigurationSection node = super.getDefaultConfig();
        node.set("velocity", 1.0);
        node.set(SkillSetting.DAMAGE.node(), 4.0);
        node.set(SkillSetting.DAMAGE_INCREASE.node(), 0.0);
        node.set("arrows", 8);
        node.set("arrows-per-level", 0);
        node.set("period", 750);
        return node;
    }
    
	@Override
    public SkillResult use(Hero hero, String args[]) 
	{
    	final Player p = hero.getPlayer();
    	if(!p.getItemInHand().getType().equals(Material.BOW)) return SkillResult.CANCELLED;
    	
   // 	final double velocity = ((SkillConfigManager.getUseSetting(hero, this, "velocity", 1.0, false)));
  //  	final double damage = ((SkillConfigManager.getUseSetting(hero, this, SkillSetting.DAMAGE.node(), 4.0, false)))
  //  			+((SkillConfigManager.getUseSetting(hero, this, SkillSetting.DAMAGE_INCREASE.node(), 0.0, false))*hero.getLevel());
    	final int arrows = ((SkillConfigManager.getUseSetting(hero, this, "arrows", 8, false)))
    			+((SkillConfigManager.getUseSetting(hero, this, "arrows-per-level", 0, false))*hero.getLevel());
    	
    	final long period = ((SkillConfigManager.getUseSetting(hero, this, "period", 750, false)));

    	final int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.plugin, new Runnable()
    	{
			@Override
			public void run() 
			{	
				shotArrows.add(p.launchProjectile(Arrow.class));
			}
    	}, 0, period);
    	Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
    	{
			@Override
			public void run() 
			{
				Bukkit.getScheduler().cancelTask(id);
			}
    	}, period*arrows+(period/2));
    	
        return SkillResult.NORMAL;
    }
	
    public class SkillHeroListener implements Listener 
    {
        private SkillArrowStorm skill;
        public SkillHeroListener(SkillArrowStorm skill) 
        {
            this.skill = skill;
        }
        
/*        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        public void onPlayerPickupItem(PlayerPickupItemEvent event) 
        {
        	if(!event.getItem().getType().equals(Material.ARROW)) return;        	
//        	for(Arrow i : skill.shotArrows)
//        		if(event.getItem().getEntityId() == i.getEntityId())
        			event.setCancelled(true);
        }*/
        
        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        public void onWeaponDamage(WeaponDamageEvent event) 
        {
        	Player p = null;
    		if(event.getAttackerEntity() instanceof Player)
    			p = (Player) event.getAttackerEntity();
    		
    		if(event.isProjectile())
    			if(event.getAttackerEntity() instanceof Projectile)
    				if(((Projectile) event.getAttackerEntity()).getShooter() instanceof Player)
    				{
    					Arrow arrow = (Arrow) event.getAttackerEntity();
    					p = (Player) ((Projectile)event.getAttackerEntity()).getShooter();
    					Hero hero = skill.plugin.getCharacterManager().getHero(p);
    					
    					double damage = ((SkillConfigManager.getUseSetting(hero, skill, SkillSetting.DAMAGE.node(), 4.0, false)))
    							+((SkillConfigManager.getUseSetting(hero, skill, SkillSetting.DAMAGE_INCREASE.node(), 0.0, false))*hero.getLevel());
    					
    					if(skill.shotArrows.contains(arrow))
    					{
    						event.setDamage(damage);
    						skill.shotArrows.remove(arrow);
    					}
    				}
        }
    }
}
