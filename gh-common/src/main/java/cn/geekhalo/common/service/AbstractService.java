package cn.geekhalo.common.service;

import cn.geekhalo.common.constants.CodeEnum;
import cn.geekhalo.common.ddd.support.AggregateRoot;
import cn.geekhalo.common.ddd.support.BaseRepository;
import cn.geekhalo.common.exception.BusinessException;
import com.google.common.base.Preconditions;
import lombok.Value;
import org.slf4j.Logger;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class AbstractService implements IService{

    private final Logger logger;

    protected AbstractService(Logger logger) {
        this.logger = logger;
    }

    protected <ID, T extends AggregateRoot> Creator<ID, T> creatorFor(BaseRepository<T,ID> repository){
        return new Creator<ID, T>(repository);
    }

    protected <ID, T extends AggregateRoot> Updater<ID, T> updaterFor(BaseRepository<T,ID> repository){
        return new Updater<ID, T>(repository);
    }


    protected class Creator<ID, T extends AggregateRoot>{
        private final BaseRepository<T,ID> repository;
        private Supplier<T> instanceFun;
        private Consumer<T> updater = a->{};
        private Consumer<T> successFun = a -> logger.info("success to save ");
        private BiConsumer<T, Exception> errorFun = (a, e) -> {
            logger.error("failed to save {}.", a, e);
            if(BusinessException.class.isAssignableFrom(e.getClass())){
                throw (BusinessException)e;
            }
            throw new BusinessException(CodeEnum.SaveError);
        };

        Creator(BaseRepository<T,ID> repository) {
            Preconditions.checkArgument(repository != null);
            this.repository = repository;
        }

        public Creator<ID, T> instance(Supplier<T> instanceFun){
            Preconditions.checkArgument(instanceFun != null);
            this.instanceFun = instanceFun;
            return this;
        }

        public Creator<ID, T> update(Consumer<T> updater){
            Preconditions.checkArgument(updater != null);
            this.updater = this.updater.andThen(updater);
            return this;
        }

        public Creator<ID, T> onSuccess(Consumer<T> onSuccessFun){
            Preconditions.checkArgument(onSuccessFun != null);
            this.successFun = onSuccessFun.andThen(this.successFun);
            return this;
        }

        public Creator<ID, T> onError(BiConsumer<T, Exception> errorFun){
            Preconditions.checkArgument(errorFun != null);
            this.errorFun = errorFun.andThen(this.errorFun);
            return this;
        }

        public T call(){
            Preconditions.checkArgument(this.instanceFun != null, "instance fun can not be null");
            Preconditions.checkArgument(this.repository != null, "repository can not be null");
            T a = null;
            try{
                a = this.instanceFun.get();

                this.updater.accept(a);

                this.repository.save(a);

                this.successFun.accept(a);
            }catch (Exception e){
                this.errorFun.accept(a, e);
            }
            return a;
        }
    }

    protected class Updater<ID, T extends AggregateRoot> {
        private final BaseRepository<T,ID> repository;
        private ID id;
        private Supplier<Optional<T>> loader;
        private Consumer<ID> onNotExistFun = id-> {throw new BusinessException(CodeEnum.NotFindError);};
        private Consumer<T> updater = a->{};
        private Consumer<Data> successFun = a -> logger.info("success to update {}", a.getId());
        private BiConsumer<Data, Exception> errorFun = (a, e) -> {
            logger.error("failed to update {}.{}", a, e);
            if(BusinessException.class.isAssignableFrom(e.getClass())){
                throw (BusinessException)e;
            }
            throw new BusinessException(CodeEnum.UpdateError);
        };

        Updater(BaseRepository<T,ID> repository) {
            this.repository = repository;
        }

        public Updater<ID, T> id(ID id){
            Preconditions.checkArgument(id != null);
            this.id = id;
            return this;
        }

        public Updater<ID, T> loader(Supplier<Optional<T>> loader){
            Preconditions.checkArgument(loader != null);
            this.loader = loader;
            return this;
        }

        public Updater<ID, T> update(Consumer<T> updater){
            Preconditions.checkArgument(updater != null);
            this.updater = updater.andThen(this.updater);
            return this;
        }


        public Updater<ID, T> onSuccess(Consumer<Data> onSuccessFun){
            Preconditions.checkArgument(onSuccessFun != null);
            this.successFun = onSuccessFun.andThen(this.successFun);
            return this;
        }

        public Updater<ID, T> onError(BiConsumer<Data, Exception> errorFun){
            Preconditions.checkArgument(errorFun != null);
            this.errorFun = errorFun.andThen(this.errorFun);
            return this;
        }


        public Updater<ID, T> onNotExist(Consumer<ID> onNotExistFun){
            Preconditions.checkArgument(onNotExistFun != null);
            this.onNotExistFun = onNotExistFun.andThen(this.onNotExistFun);
            return this;
        }

        public T call(){
            Preconditions.checkArgument(this.repository != null, "repository can not be null");
            Preconditions.checkArgument((this.loader != null || this.id != null), "id and loader can not both be null");
            T a = null;
            try {
                if (id != null && loader != null){
                    throw new RuntimeException("id and loader can both set");
                }
                if (id != null){
                    this.loader = ()->this.repository.findById(id);
                }
                Optional<T> aOptional = this.loader.get();

                if (!aOptional.isPresent()){
                    this.onNotExistFun.accept(id);
                }
                a = aOptional.get();
                updater.accept(a);

                this.repository.save(a);

                this.successFun.accept(new Data(id, a));

            }catch (Exception e){
                this.errorFun.accept(new Data(id, a), e);
            }
            return a;
        }

        @Value
        public class Data{
            private final ID id;
            private final T entity;
        }
    }
}
