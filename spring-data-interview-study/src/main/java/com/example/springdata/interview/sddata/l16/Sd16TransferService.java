package com.example.springdata.interview.sddata.l16;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class Sd16TransferService {

    private final Sd16WalletRepository walletRepository;

    public Sd16TransferService(Sd16WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    /** Orchestration and transaction boundary live on the service, not the repository. */
    @Transactional
    public void transfer(Long fromId, Long toId, int cents) {
        Sd16Wallet from = walletRepository.findById(fromId).orElseThrow();
        Sd16Wallet to = walletRepository.findById(toId).orElseThrow();
        from.setBalanceCents(from.getBalanceCents() - cents);
        to.setBalanceCents(to.getBalanceCents() + cents);
        walletRepository.save(from);
        walletRepository.save(to);
    }
}
