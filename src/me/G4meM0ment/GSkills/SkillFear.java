package me.G4meM0ment.GSkills;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

import com.herocraftonline.heroes.Heroes;
import com.herocraftonline.heroes.api.SkillResult;
import com.herocraftonline.heroes.characters.Hero;
import com.herocraftonline.heroes.characters.effects.EffectType;
import com.herocraftonline.heroes.characters.effects.PeriodicEffect;
import com.herocraftonline.heroes.characters.skill.ActiveSkill;
import com.herocraftonline.heroes.characters.skill.SkillConfigManager;
import com.herocraftonline.heroes.characters.skill.SkillSetting;
import com.herocraftonline.heroes.characters.skill.SkillType;

public class SkillFear extends ActiveSkill{
	
    private String applyText;
    private String expireText;
	
    public SkillFear(Heroes plugin) {
        super(plugin, "Fear");
        setUsage("/skill fear");
        setDescription("Make mobs fear in an area of $1% for $2 milliseconds");
        setTypes(SkillType.INTERRUPT, SkillType.FORCE);
        setArgumentRange(0, 0);
        setIdentifiers("skill fear");
    }
    
    @Override
    public void init() {
        super.init();
        applyText = SkillConfigManager.getRaw(this, "on-text", "%hero% chills the air in an %skill%!").replace("%hero%", "$1").replace("%skill%", "$2");
        expireText = SkillConfigManager.getRaw(this, "off-text", "%hero% stops his %skill%!").replace("%hero%", "$1").replace("%skill%", "$2");
    }

    @Override
    public String getDescription(Hero hero) {
    	
    	double maxDistance = ((SkillConfigManager.getUseSetting(hero, this, SkillSetting.MAX_DISTANCE, 15, false)) +
    			((SkillConfigManager.getUseSetting(hero, this, SkillSetting.MAX_DISTANCE_INCREASE, 0.5, false)) * hero.getSkillLevel(this)));
    	int duration = 	(SkillConfigManager.getUseSetting(hero, this, SkillSetting.DURATION, 5000, false)) +
    			((SkillConfigManager.getUseSetting(hero, this, SkillSetting.DURATION_INCREASE, 100, false)) * hero.getSkillLevel(this));
    	
        String description = getDescription().replace("$1", maxDistance + "").replace("$2", duration + "");
        return description;
    }

    @Override
    public ConfigurationSection getDefaultConfig() {
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
    public SkillResult use(Hero hero, String args[]) {
        if (hero.hasEffect("Fear")) {
            hero.removeEffect(hero.getEffect("Fear"));
        } else {
            long period = SkillConfigManager.getUseSetting(hero, this, SkillSetting.PERIOD.node(), 5000, false) +
        			((SkillConfigManager.getUseSetting(hero, this, "period-increase", 100, false)) * hero.getSkillLevel(this));
            int range = (int) (SkillConfigManager.getUseSetting(hero, this, SkillSetting.RADIUS.node(), 15.0, false) +
                (SkillConfigManager.getUseSetting(hero, this, "radius-increase", 0.5, false) * hero.getSkillLevel(this)));
            int mana = (int) (SkillConfigManager.getUseSetting(hero, this, SkillSetting.MANA.node(), 20, false));
            
            hero.addEffect(new FearEffect(this, period, range, mana));
        }
        return SkillResult.NORMAL;
    }
    
    public class FearEffect extends PeriodicEffect {

        private int range;
        private int mana;
        private boolean firstTime = true;

        public FearEffect(SkillFear skill, long period, int range, int manaLoss) {
            super(skill, "Fear", period);
            this.range = range;
            this.mana = manaLoss;
            this.types.add(EffectType.UNTARGETABLE);
            this.types.add(EffectType.BENEFICIAL);
        }

        @Override
        public void applyToHero(Hero hero) {
            firstTime = true;
            super.applyToHero(hero);
            Player player = hero.getPlayer();
            broadcast(player.getLocation(), applyText, player.getDisplayName(), "Fear");
        }

        @Override
        public void removeFromHero(Hero hero) {
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
                if(entity instanceof Monster) 
                {
                    Monster mob = (Monster) entity;
                    mob.setTarget(null);
                }
            }
            if(hero.getMaxMana() <= 0) return;
            if(mana > 0 && !firstTime) {
                if(hero.getMana() - mana < 0) {
                    hero.setMana(0);
                } else {
                    hero.setMana(hero.getMana() - mana);
                }
            } else if(firstTime) {
                firstTime = false;
            }
            
            if(hero.getMana() < mana) {
                hero.removeEffect(this);
            }
        }
    }
}
