package org.jurassicraft.server.entity;

import java.util.ArrayList;
import java.util.List;

public enum DinosaurStatus {
    CARNIVOROUS {
        @Override
        public boolean apply(DinosaurEntity entity, DinosaurEntity.FieldGuideInfo info) {
            return entity.getDinosaur().getDiet().isCarnivorous();
        }
    },
    HERBIVOROUS {
        @Override
        public boolean apply(DinosaurEntity entity, DinosaurEntity.FieldGuideInfo info) {
            return entity.getDinosaur().getDiet().isHerbivorous();
        }
    },
    DIURNAL {
        @Override
        public boolean apply(DinosaurEntity entity, DinosaurEntity.FieldGuideInfo info) {
            return entity.getDinosaur().getSleepingSchedule() == SleepingSchedule.DIURNAL;
        }
    },
    NOCTURNAL {
        @Override
        public boolean apply(DinosaurEntity entity, DinosaurEntity.FieldGuideInfo info) {
            return entity.getDinosaur().getSleepingSchedule() == SleepingSchedule.NOCTURNAL;
        }
    },
    CREPUSCULAR {
        @Override
        public boolean apply(DinosaurEntity entity, DinosaurEntity.FieldGuideInfo info) {
            return entity.getDinosaur().getSleepingSchedule() == SleepingSchedule.CREPUSCULAR;
        }
    },
    TAMED {
        @Override
        public boolean apply(DinosaurEntity entity, DinosaurEntity.FieldGuideInfo info) {
            return entity.getOwner() != null;
        }
    },
    LOW_HEALTH {
        @Override
        public boolean apply(DinosaurEntity entity, DinosaurEntity.FieldGuideInfo info) {
            return entity.getHealth() < entity.getMaxHealth() / 4 || entity.isCarcass();
        }
    },
    HUNGRY {
        @Override
        public boolean apply(DinosaurEntity entity, DinosaurEntity.FieldGuideInfo info) {
            return info.hungry;
        }
    },
    THIRSTY {
        @Override
        public boolean apply(DinosaurEntity entity, DinosaurEntity.FieldGuideInfo info) {
            return info.thirsty;
        }
    },
    POISONED {
        @Override
        public boolean apply(DinosaurEntity entity, DinosaurEntity.FieldGuideInfo info) {
            return info.poisoned;
        }
    },
    DROWNING {
        @Override
        public boolean apply(DinosaurEntity entity, DinosaurEntity.FieldGuideInfo info) {
            return !entity.getDinosaur().isMarineAnimal() && entity.getAir() < 200;
        }
    },
    SLEEPY {
        @Override
        public boolean apply(DinosaurEntity entity, DinosaurEntity.FieldGuideInfo info) {
            return entity.shouldSleep();
        }
    },
    FLOCKING {
        @Override
        public boolean apply(DinosaurEntity entity, DinosaurEntity.FieldGuideInfo info) {
            return info.flocking;
        }
    },
    SCARED {
        @Override
        public boolean apply(DinosaurEntity entity, DinosaurEntity.FieldGuideInfo info) {
            return info.scared;
        }
    };

    public static List<DinosaurStatus> getActiveStatuses(DinosaurEntity entity, DinosaurEntity.FieldGuideInfo info) {
        List<DinosaurStatus> statuses = new ArrayList<>();

        for (DinosaurStatus status : values()) {
            if (status.apply(entity, info)) {
                statuses.add(status);
            }
        }

        return statuses;
    }

    public abstract boolean apply(DinosaurEntity entity, DinosaurEntity.FieldGuideInfo info);
}
