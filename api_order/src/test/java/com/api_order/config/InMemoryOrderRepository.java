package com.api_order.config;

import com.api_order.model.order.OrderModel;
import com.api_order.repository.OrderRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class InMemoryOrderRepository implements OrderRepository {

    private final Map<UUID, OrderModel> store = new ConcurrentHashMap<>();

    @Override
    public <S extends OrderModel> S save(S entity) {
        if (entity.getId() == null) {
            entity.setId(UUID.randomUUID());
        }
        store.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Optional<OrderModel> findById(UUID uuid) {
        return Optional.ofNullable(store.get(uuid));
    }

    @Override
    public Page<OrderModel> findAll(Pageable pageable) {
        List<OrderModel> orders = new ArrayList<>(store.values());
        return new org.springframework.data.domain.PageImpl<>(orders, pageable, orders.size());
    }

    // Os outros métodos do OrderRepository podem lançar UnsupportedOperationException
    @Override
    public void delete(OrderModel entity) {
        store.remove(entity.getId());
    }

    @Override
    public void deleteAllById(Iterable<? extends UUID> uuids) {

    }

    @Override
    public boolean existsById(UUID uuid) {
        return store.containsKey(uuid);
    }

    // Para simplificar, outros métodos não implementados:
    @Override
    public List<OrderModel> findAll() { throw new UnsupportedOperationException(); }
    @Override
    public long count() { return store.size(); }
    @Override
    public void deleteById(UUID uuid) { store.remove(uuid); }
    @Override
    public void deleteAll() { store.clear(); }
    @Override
    public void deleteAll(Iterable<? extends OrderModel> entities) { throw new UnsupportedOperationException(); }
    @Override
    public <S extends OrderModel> List<S> saveAll(Iterable<S> entities) { throw new UnsupportedOperationException(); }
    @Override
    public List<OrderModel> findAllById(Iterable<UUID> uuids) { throw new UnsupportedOperationException(); }

    @Override
    public void flush() {

    }

    @Override
    public <S extends OrderModel> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends OrderModel> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<OrderModel> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<UUID> uuids) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public OrderModel getOne(UUID uuid) {
        return null;
    }

    @Override
    public OrderModel getById(UUID uuid) {
        return null;
    }

    @Override
    public OrderModel getReferenceById(UUID uuid) {
        return null;
    }

    @Override
    public <S extends OrderModel> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends OrderModel> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends OrderModel> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends OrderModel> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends OrderModel> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends OrderModel> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends OrderModel, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public List<OrderModel> findAll(Sort sort) {
        return List.of();
    }
}
