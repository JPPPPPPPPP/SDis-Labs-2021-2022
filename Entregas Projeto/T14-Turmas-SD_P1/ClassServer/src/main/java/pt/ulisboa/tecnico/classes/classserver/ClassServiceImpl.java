package pt.ulisboa.tecnico.classes.classserver;

import pt.ulisboa.tecnico.classes.contract.classserver.ClassServerServiceGrpc;

public class ClassServiceImpl extends ClassServerServiceGrpc.ClassServerServiceImplBase {
    private Classe classe;

    public ClassServiceImpl(Classe c) {
        this.classe = c;
    }

}
