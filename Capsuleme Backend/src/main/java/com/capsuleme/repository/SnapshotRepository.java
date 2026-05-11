package com.capsuleme.repository;

import com.capsuleme.model.Snapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SnapshotRepository extends JpaRepository<Snapshot, Long> {

    List<Snapshot> findByCapsuleId(Long capsuleId);

    Optional<Snapshot> findByCapsuleIdAndType(Long capsuleId, Snapshot.SnapshotType type);
}
