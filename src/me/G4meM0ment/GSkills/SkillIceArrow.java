package me.G4meM0ment.GSkills;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.herocraftonline.heroes.Heroes;
import com.herocraftonline.heroes.api.SkillResult;
import com.herocraftonline.heroes.api.events.WeaponDamageEvent;
import com.herocraftonline.heroes.characters.Hero;
import com.herocraftonline.heroes.characters.effects.Effect;
import com.herocraftonline.heroes.characters.effects.EffectType;
import com.herocraftonline.heroes.characters.skill.ActiveSkill;
import com.herocraftonline.heroes.characters.skill.Skill;
import com.herocraftonline.heroes.characters.skill.SkillConfigManager;
import com.herocraftonline.heroes.characters.skill.SkillSetting;
import com.herocraftonline.heroes.characters.skill.SkillType;

public class SkillIceArrow extends ActiveSkill {
	
	public Arrow shotArrow;
	public int duration;
	public int multiplier;
	
	public SkillIceArrow(Heroes plugin) 
    {
        super(plugin, "IceArrow");
        setUsage("/skill icearrow");
        setDescription("You shoot a arrow embraced with runes, meaning 'endless ice'");
        setTypes(SkillType.ICE);
        setArgumentRange(0, 0);
        setIdentifiers("skill icearrow");
        Bukkit.getServer().getPluginManager().registerEvents(new SkillHeroListener(this), plugin);
    }

    @Override
    public String getDescription(Hero hero) 
    {
    	long duration = SkillConfigManager.getUseSetting(hero, this, SkillSetting.DURATION.node(), 5000, false)
    			+(SkillConfigManager.getUseSetting(hero, this, SkillSetting.DURATION_INCREASE.node(), 1000, false)*hero.getLevel());
    	int multiplier = SkillConfigManager.getUseSetting(hero, this, "speed-multiplier", 5000, false)
    			+(SkillConfigManager.getUseSetting(hero, this, "speed-multiplier-increase", 1000, false)*hero.getLevel());
    	
        String description = getDescription().replace("$1", duration+"").replace("$2", multiplier+"");
        return description;
    }

    @Override
    public ConfigurationSection getDefaultConfig() 
    {
        ConfigurationSection node = super.getDefaultConfig();
        node.set(SkillSetting.DURATION.node(), 0.0);
        node.set(SkillSetting.DURATION_INCREASE.node(), 0.0);
        node.set("speed-multiplier", 2);
        node.set("speed-multiplier-increase", 0);
        return node;
    }
    
	@Override
    public SkillResult use(Hero hero, String args[]) 
	{
//    	final Player p = hero.getPlayer();
//   	if(!p.getItemInHand().getType().equals(Material.BOW)) return SkillResult.CANCELLED;

    	duration = SkillConfigManager.getUseSetting(hero, this, SkillSetting.DURATION.node(), 5000, false)
    			+(SkillConfigManager.getUseSetting(hero, this, SkillSetting.DURATION_INCREASE.node(), 1000, false)*hero.getLevel());
    	multiplier = SkillConfigManager.getUseSetting(hero, this, "speed-multiplier", 2, false)
    			+(SkillConfigManager.getUseSetting(hero, this, "speed-multiplier-increase", 0, false)*hero.getLevel());
    	
    	hero.addEffect(new IceArrowEffect(plugin, this, hero));
    	
        return SkillResult.NORMAL;
    }
	
	public class IceArrowEffect extends Effect
	{

		public IceArrowEffect(Heroes plugin, Skill skill, Hero hero) 
		{
			super(skill, "IceArrowEffect");
			
			this.types.add(EffectType.ICE);
		}
		
        @Override
        public void applyToHero(Hero hero) 
        {
            super.applyToHero(hero);
  //         Player player = hero.getPlayer();
  //         broadcast(player.getLocation(), applyText, player.getDisplayName(), "IceArrow");
        }

        @Override
        public void removeFromHero(Hero hero) 
        {
            super.removeFromHero(hero);
    //        Player player = hero.getPlayer();
//            broadcast(player.getLocation(), applyText, player.getDisplayName(), "IceArrow");
        }
	}
	
    public class SkillHeroListener implements Listener 
    {
        private SkillIceArrow skill;
        public SkillHeroListener(SkillIceArrow skill) 
        {
            this.skill = skill;
        }
        
        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        public void onEntityShootBow(EntityShootBowEvent event) 
        {
        	if(!(event.getEntity() instanceof Player) || !(event.getProjectile() instanceof Arrow)) return;
        	
        	Hero h = skill.plugin.getCharacterManager().getHero((Player)event.getEntity()); 
        	if(h.hasEffect("IceArrowEffect"))
        	{
//        		h.getPlayer().sendMessage(ChatColor.GRAY+"["+ChatColor.DARK_GREEN+"Skill"+ChatColor.GRAY+"]"+" Eispfeil geschossen");
        		skill.shotArrow = (Arrow) event.getProjectile();
        		h.getPlayer().getWorld().playSound(h.getPlayer().getLocation(), Sound.BREATH, 10, 1);
        		h.removeEffect(h.getEffect("IceArrowEffect"));
        	}
        }
        
        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        public void onWeaponDamage(WeaponDamageEvent event) 
        {    		
        	if(skill.shotArrow == null) return;
    		if(event.isProjectile())
    			if(event.getAttackerEntity() instanceof Projectile && event.getEntity() instanceof LivingEntity)
    				if(((Projectile) event.getAttackerEntity()).getShooter() instanceof Player)
    				{
    					Arrow arrow = (Arrow) event.getAttackerEntity();
    					
    					if(skill.shotArrow.equals(arrow))
    					{
    						LivingEntity e = (LivingEntity) event.getEntity();
    						e.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, skill.duration/50, skill.multiplier, false));
    						e.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, skill.duration/50, -skill.multiplier, false));
    					}
    				}
        }
    }
    
    
}
