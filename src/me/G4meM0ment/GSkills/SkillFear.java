package me.G4meM0ment.GSkills;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;

import com.herocraftonline.heroes.Heroes;
import com.herocraftonline.heroes.api.SkillResult;
import com.herocraftonline.heroes.characters.Hero;
import com.herocraftonline.heroes.characters.effects.EffectType;
import com.herocraftonline.heroes.characters.effects.PeriodicEffect;
import com.herocraftonline.heroes.characters.skill.ActiveSkill;
import com.herocraftonline.heroes.characters.skill.Skill;
import com.herocraftonline.heroes.characters.skill.SkillConfigManager;
import com.herocraftonline.heroes.characters.skill.SkillSetting;
import com.herocraftonline.heroes.characters.skill.SkillType;

public class SkillFear extends ActiveSkill{
	
    private String applyText;
    private String expireText;
	
    public SkillFear(Heroes plugin) 
    {
        super(plugin, "Fear");
        setUsage("/skill fear");
        setDescription("Make mobs fear in an area of $1% for $2 milliseconds");
        setTypes(SkillType.INTERRUPT, SkillType.FORCE);
        setArgumentRange(0, 0);
        setIdentifiers("skill fear");
        
        Bukkit.getServer().getPluginManager().registerEvents(new SkillHeroListener(this), plugin);
    }
    
    @Override
    public void init() 
    {
        super.init();
        applyText = SkillConfigManager.getRaw(this, "on-text", "%hero% chills the air in an %skill%!").replace("%hero%", "$1").replace("%skill%", "$2");
        expireText = SkillConfigManager.getRaw(this, "off-text", "%hero% stops his %skill%!").replace("%hero%", "$1").replace("%skill%", "$2");
    }

    @Override
    public String getDescription(Hero hero)
    {
    	
    	double maxDistance = ((SkillConfigManager.getUseSetting(hero, this, SkillSetting.MAX_DISTANCE, 15, false)) +
    			((SkillConfigManager.getUseSetting(hero, this, SkillSetting.MAX_DISTANCE_INCREASE, 0.5, false)) * hero.getSkillLevel(this)));
    	int duration = 	(SkillConfigManager.getUseSetting(hero, this, SkillSetting.DURATION, 5000, false)) +
    			((SkillConfigManager.getUseSetting(hero, this, SkillSetting.DURATION_INCREASE, 100, false)) * hero.getSkillLevel(this));
    	
        String description = getDescription().replace("$1", maxDistance + "").replace("$2", duration + "");
        return description;
    }

    @Override
    public ConfigurationSection getDefaultConfig() 
    {
        ConfigurationSection node = super.getDefaultConfig();
        node.set("on-text", "%hero% fears the enemys with his %skill%ing aura!");
        node.set("off-text", "%hero% stops %skill%ing!");
        node.set(SkillSetting.MAX_DISTANCE.node(), 15);
        node.set(SkillSetting.MAX_DISTANCE_INCREASE.node(), 0.5);
        node.set(SkillSetting.PERIOD.node(), 5000);
        node.set("period-increase", 100);
        node.set(SkillSetting.MANA.node(), 20);
        return node;
    }
    
    @Override
    public SkillResult use(Hero hero, String args[]) 
    {
    	Player player = hero.getPlayer();
        if (hero.hasEffect("Fear"))
        {
            hero.removeEffect(hero.getEffect("Fear"));
            return SkillResult.REMOVED_EFFECT;
        }

        long period = SkillConfigManager.getUseSetting(hero, this, SkillSetting.PERIOD.node(), 5000, false) +
        		((SkillConfigManager.getUseSetting(hero, this, "period-increase", 100, false)) * hero.getSkillLevel(this));
        int range = (int) (SkillConfigManager.getUseSetting(hero, this, SkillSetting.RADIUS.node(), 15.0, false) +
        		(SkillConfigManager.getUseSetting(hero, this, "radius-increase", 0.5, false) * hero.getSkillLevel(this)));
        int mana = (int) (SkillConfigManager.getUseSetting(hero, this, SkillSetting.MANA.node(), 20, false));
            
        hero.addEffect(new FearEffect(this, period, range, mana));
        
        for(Entity entity : player.getNearbyEntities(range, range, range)) 
        {
            if(entity instanceof Creature) 
            {
                Creature mob = (Creature) entity;
                boolean set = false;
                for(Entity pp : player.getNearbyEntities(range+10, range+10, range+10))
                {
                	if(pp instanceof Player)
                	{
                		if(pp == player)
                			continue;
                		mob.setTarget((Player) pp);
                		set = true;
                		break;
                	}
                	mob.setTarget(null);
                	set = true;
                }
                if(!set)
                	mob.setTarget(null);
            }
        }
        
        return SkillResult.NORMAL;
    }
    
    public class FearEffect extends PeriodicEffect {

        private int range;
        private int mana;
        private long start;
        private long period;
        private boolean firstTime = true;

        public FearEffect(SkillFear skill, long period, int range, int manaLoss) 
        {
            super(skill, "Fear", period);
            this.range = range;
            this.mana = manaLoss;
            this.period = period;
            this.types.add(EffectType.UNTARGETABLE);
            this.types.add(EffectType.BENEFICIAL);
        }

        @Override
        public void applyToHero(Hero hero) 
        {
            firstTime = true;
            start = System.currentTimeMillis();
            super.applyToHero(hero);
            Player player = hero.getPlayer();
            broadcast(player.getLocation(), applyText, player.getDisplayName(), "Fear");
        }

        @Override
        public void removeFromHero(Hero hero) 
        {
            super.removeFromHero(hero);
            Player player = hero.getPlayer();
            broadcast(player.getLocation(), expireText, player.getDisplayName(), "Fear");
        }

        @Override
        public void tickHero(Hero hero) 
        {
            super.tickHero(hero);
            Player player = hero.getPlayer();

            for(Entity entity : player.getNearbyEntities(range, range, range)) 
            {
                if(entity instanceof Creature) 
                {
                    Creature mob = (Creature) entity;
                    boolean set = false;
                    for(Entity pp : player.getNearbyEntities(range+10, range+10, range+10))
                    {
                    	if(pp instanceof Player)
                    	{
                    		if(pp == player)
                    			continue;
                    		mob.setTarget((Player) pp);
                    		set = true;
                    		break;
                    	}
                    	mob.setTarget(null);
                    	set = true;
                    }
                    if(!set)
                    	mob.setTarget(null);
                }
            }
            if(hero.getMaxMana() <= 0 || mana <= 0) 
            {
            	if(System.currentTimeMillis()-start > period)
            		hero.removeEffect(this);
            	return;
            }
            if(mana > 0 && !firstTime) 
            {
                if(hero.getMana() - mana < 0)
                    hero.setMana(0);
                else
                    hero.setMana(hero.getMana() - mana);
            } 
            else if(firstTime)
                firstTime = false;
            
            if(hero.getMana() < mana)
                hero.removeEffect(this);
        }
    }
    
    public class SkillHeroListener implements Listener 
    {
        private Skill skill;
        public SkillHeroListener(Skill skill) 
        {
            this.skill = skill;
        }
        
        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        public void onEntityTarget(EntityTargetEvent event) 
        {
        	if(event.getTarget() instanceof Player)
        	{
        		Hero h = skill.plugin.getCharacterManager().getHero((Player) event.getTarget());
        		if(h == null) return;
        		if(h.hasEffect("Fear"))
        		{
        			event.setTarget(null);
        			event.setCancelled(true);
        		}
        	}
        }
        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        public void onEntityTargetLivingEntity(EntityTargetLivingEntityEvent event) 
        {
        	if(event.getTarget() instanceof Player)
        	{
        		Hero h = skill.plugin.getCharacterManager().getHero((Player) event.getTarget());
        		if(h == null) return;
        		if(h.hasEffect("Fear"))
        		{
        			event.setTarget(null);
        			event.setCancelled(true);
        		}
        	}
        }
        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        public void onExplosionPrime(ExplosionPrimeEvent event) 
        {
        	if(event.getEntity() instanceof Creeper)
        	{
        		Entity target = ((Creeper) event.getEntity()).getTarget();
        		if(target == null) return;
        		if(!(target instanceof Player)) return;
        		Player p = (Player) target;
        		Hero h = skill.plugin.getCharacterManager().getHero(p);
        		if(h == null) return;
        		if(h.hasEffect("Fear"))
        			event.setCancelled(true);
        	}
        }
    }
}
