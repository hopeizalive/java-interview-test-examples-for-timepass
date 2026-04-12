package com.example.springdata.interview.sddata.l15;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class Sd15LazyDemoService {

    private final Sd15TeamRepository teamRepository;

    public Sd15LazyDemoService(Sd15TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Transactional(readOnly = true)
    public Sd15Team loadTeamInTx(Long id) {
        return teamRepository.findById(id).orElseThrow();
    }

    /** Outside a transaction: lazy collection is not initialized. */
    public int countPlayersOutsideTx(Long teamId) {
        Sd15Team team = loadTeamInTx(teamId);
        return team.getPlayers().size();
    }

    @Transactional(readOnly = true)
    public int countPlayersInsideTx(Long teamId) {
        Sd15Team team = teamRepository.findById(teamId).orElseThrow();
        return team.getPlayers().size();
    }
}
