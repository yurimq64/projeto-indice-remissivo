import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class IndiceRemissivo {

    public static void main(String[] args) {

        String arquivoPalavrasChave = "chaves.txt";
        String arquivoTextoBase = "texto.txt";
        String arquivoSaida = "indice_remissivo.txt";

        HashColisaoExterior hash = new HashColisaoExterior(26);

        try (BufferedReader br = new BufferedReader(new FileReader(arquivoPalavrasChave))) {
            String linha;
            while ((linha = br.readLine()) != null) {

                Scanner scannerLinha = new Scanner(linha);

                while (scannerLinha.hasNext()) {
                    String palavraChave = scannerLinha.next();

                    String palavraLimpa = palavraChave.toLowerCase();
                    palavraLimpa = palavraLimpa.replace(",", "");
                    palavraLimpa = palavraLimpa.replace(".", "");
                    palavraLimpa = palavraLimpa.replace("!", "");
                    palavraLimpa = palavraLimpa.replace("?", "");
                    palavraLimpa = palavraLimpa.replace(";", "");
                    palavraLimpa = palavraLimpa.replace(":", "");
                    palavraLimpa = palavraLimpa.replace("(", "");
                    palavraLimpa = palavraLimpa.replace(")", "");

                    if (!palavraLimpa.isEmpty()) {
                        hash.insere(palavraLimpa);
                    }
                }
                scannerLinha.close();
            }
            System.out.println("1. Chaves carregadas.");
        } catch (IOException e) {
            System.err.println("Erro ao ler " + e.getMessage());
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(arquivoTextoBase))) {
            String linha;
            int numeroLinha = 1;

            while ((linha = br.readLine()) != null) {

                Scanner scannerLinha = new Scanner(linha);

                while (scannerLinha.hasNext()) {
                    String palavraSuja = scannerLinha.next();

                    String palavraLimpa = palavraSuja.toLowerCase();
                    palavraLimpa = palavraLimpa.replace(",", "");
                    palavraLimpa = palavraLimpa.replace(".", "");
                    palavraLimpa = palavraLimpa.replace("!", "");
                    palavraLimpa = palavraLimpa.replace("?", "");
                    palavraLimpa = palavraLimpa.replace(";", "");
                    palavraLimpa = palavraLimpa.replace(":", "");
                    palavraLimpa = palavraLimpa.replace("(", "");
                    palavraLimpa = palavraLimpa.replace(")", "");

                    if (!palavraLimpa.isEmpty()) {
                        char primeiraLetra = palavraLimpa.charAt(0);
                        if (primeiraLetra >= 'a' && primeiraLetra <= 'z') {
                            ArvoreBinariaBusca.Nodo pc = hash.busca(palavraLimpa);

                            if (pc != null) {
                                pc.ocorrencias.insereFinal(numeroLinha);
                            }
                        }
                    }
                }
                scannerLinha.close();

                numeroLinha++;
            }
            System.out.println("Texto processado.");
            hash.imprime();
        }
        catch (IOException e) {
            System.err.println("Erro ao ler " + e.getMessage());
            return;
        }
    }

    static class ListaDuplamenteEncadeada {

        class Nodo {

            public int elemento;
            public Nodo proximo;
            public Nodo anterior;

            public Nodo(int elemento) {
                this.elemento = elemento;
                this.proximo = null;
                this.anterior = null;
            }
        }

        private Nodo inicio;
        private Nodo fim;
        private int nElementos;

        public ListaDuplamenteEncadeada() {
            this.inicio = null;
            this.fim = null;
            this.nElementos = 0;
        }

        public boolean estaVazia() {
            return this.nElementos == 0;
        }

        public int tamanho() {
            return this.nElementos;
        }

        public void imprime() {
            Nodo cursor = this.inicio;
            for(int i=0;i<this.nElementos;i++) {
                System.out.print(cursor.elemento + " ");
                cursor = cursor.proximo;
            }
        }

        public void imprimeInverso() {
            System.out.print("[");
            Nodo cursor = this.fim;
            for(int i=0;i<this.nElementos;i++) {
                System.out.print(cursor.elemento + " ");
                cursor = cursor.anterior;
            }
            System.out.println("]");
        }

        public void insereInicio(int elemento) {

            Nodo novo = new Nodo(elemento);

            if(this.estaVazia()) {
                this.fim = novo;
            } else {
                novo.proximo = this.inicio;
                this.inicio.anterior = novo;
            }

            this.inicio = novo;
            this.nElementos++;

        }

        public Integer removeInicio() {

            if(this.estaVazia()) {
                System.out.println("Lista vazia. Não é possível remover.");
                return null;
            }

            Nodo nodoRemovido = this.inicio;

            if(this.nElementos == 1) {
                this.inicio = null;
                this.fim = null;
            } else {
                this.inicio = nodoRemovido.proximo;
                this.inicio.anterior = null;

                nodoRemovido.proximo = null;
            }

            this.nElementos--;

            return nodoRemovido.elemento;
        }

        public void insereFinal(int elemento) {

            Nodo novo = new Nodo(elemento);

            if(this.estaVazia()) {
                this.inicio = novo;
            } else {
                this.fim.proximo = novo;
                novo.anterior = this.fim;
            }

            this.fim = novo;

            this.nElementos++;
        }

        public Integer removeFinal() {

            if(this.estaVazia()) {
                System.out.println("Lista vazia. Não é possível remover.");
                return null;
            }

            Nodo nodoRemovido = this.fim;

            if(this.nElementos == 1) {

                this.inicio = null;
                this.fim = null;
            } else {

                this.fim = nodoRemovido.anterior;

                nodoRemovido.anterior.proximo = null;
                nodoRemovido.anterior = null;
            }

            this.nElementos--;

            return nodoRemovido.elemento;
        }

        public void inserePosicao(int elemento, int pos) {

            if(pos < 0) {
                System.out.println("Posição negativa. Não é possível inserir.");
                return;
            } else if(pos > this.nElementos) {
                System.out.println("Posição inválida. Não é possível inserir.");
                return;
            }

            if(pos == 0) {
                this.insereInicio(elemento);
                return;
            } else if(pos == this.nElementos ) {
                this.insereFinal(elemento);
                return;
            }

            Nodo novo = new Nodo(elemento);

            Nodo cursor = this.inicio;
            for(int i=1;i<=pos;i++) {
                cursor = cursor.proximo;
            }

            novo.anterior = cursor.anterior;
            novo.proximo = cursor;

            cursor.anterior.proximo = novo;
            cursor.anterior = novo;

            this.nElementos++;

        }

        public Integer removePosicao(int pos) {

            if(this.estaVazia()) {
                System.out.println("Lista vazia. Não é possível remover.");
                return null;
            } else if(pos < 0) {
                System.out.println("Posição Negativa. Não é possível remover");
                return null;
            } else if(pos >= this.nElementos) {
                System.out.println("Posição inválida. Não é possível remover.");
                return null;
            }

            if(pos == 0) {
                return this.removeInicio();
            } else if(pos == this.nElementos-1) {
                return this.removeFinal();
            }

            Nodo cursor = this.inicio;
            for(int i=1;i<=pos;i++) {
                cursor = cursor.proximo;
            }

            Nodo nodoRemovido = cursor;

            nodoRemovido.anterior.proximo = nodoRemovido.proximo;
            nodoRemovido.proximo.anterior = nodoRemovido.anterior;

            nodoRemovido.anterior = null;
            nodoRemovido.proximo = null;

            this.nElementos--;

            return nodoRemovido.elemento;

        }

        public void insereOrdenado(int elemento) {

            if(this.estaVazia()) {
                this.insereInicio(elemento);
                return;
            }

            boolean flagInseriu = false;

            Nodo cursor = this.inicio;
            for(int i=0;i<this.nElementos;i++) {
                if(cursor.elemento > elemento) {
                    this.inserePosicao(elemento, i);
                    flagInseriu = true;
                    break;
                }
                cursor = cursor.proximo;
            }

            if(!flagInseriu) {
                this.insereFinal(elemento);
            }
        }

        public boolean removeElemento(int elemento) {

            Nodo cursor = this.inicio;
            int i;
            for (i = 0; i < this.nElementos; i++) {
                if(cursor.elemento == elemento) {
                    break;
                }
                cursor = cursor.proximo;
            }

            if(i == this.nElementos) {
                return false;
            }

            this.removePosicao(i);

            return true;

        }

        public Integer acesse(int pos) {

            if (pos < 0 || pos >= this.nElementos) {
                return null;
            }

            Nodo cursor = this.inicio;
            for (int i = 0; i < pos; i++) {
                cursor = cursor.proximo;
            }

            return cursor.elemento;

        }

        public boolean contem(int elemento) {

            Nodo cursor = this.inicio;
            for (int i = 0; i < this.nElementos; i++) {
                if(cursor.elemento == elemento) {
                    return true;
                }
                cursor = cursor.proximo;
            }

            return false;
        }

    }

    static class HashColisaoExterior {

        public ArvoreBinariaBusca vetor[];
        public int nElementos;

        public HashColisaoExterior(int capacidade) {
            this.vetor = new ArvoreBinariaBusca[capacidade];
            for (int i = 0; i < vetor.length; i++) {
                this.vetor[i] = new ArvoreBinariaBusca();
            }
            this.nElementos = 0;
        }

        public int tamanho() {
            return this.nElementos;
        }

        public void imprime() {
            for (int i = 0; i < vetor.length; i++) {
                if (!vetor[i].estaVazia()) {
                    vetor[i].imprimeEmOrdem();
                }
            }
        }

        private int funcaoHash(String chave) {
            char primeiraLetra = chave.charAt(0);
            int endereco = (int) primeiraLetra - 97;

            return endereco;
        }

        public void insere(String chave) {
            int endereco = funcaoHash(chave);
            this.vetor[endereco].insere(chave);
            this.nElementos++;
        }

//        public boolean remove(int elemento) {
//            int endereco = funcaoHash(elemento);
//            boolean removeu = this.vetor[endereco].removeElemento(elemento);
//
//            if(removeu) this.nElementos--;
//
//            return removeu;
//        }

//        public boolean contem(String chave) {
//            int endereco = funcaoHash(chave);
//            return this.vetor[endereco].contem(chave);
//        }

        public ArvoreBinariaBusca.Nodo busca(String chave) {
            int endereco = funcaoHash(chave);
            return this.vetor[endereco].buscaNo(chave);
        }
    }

    static class ArvoreBinariaBusca {

        static class Nodo {

            public String chave;
            public ListaDuplamenteEncadeada ocorrencias = new ListaDuplamenteEncadeada();
            public Nodo esquerdo;
            public Nodo direito;

            public Nodo(String chave) {
                this.chave = chave;
                this.esquerdo = null;
                this.direito = null;
            }
        }

        public Nodo raiz;
        public int nElementos;

        public ArvoreBinariaBusca() {
            this.raiz = null;
            this.nElementos = 0;
        }

        public int tamanho() {
            return this.nElementos;
        }

        public boolean estaVazia() {
            return this.raiz == null;
        }

//        public void imprimeEmLargura() {
//
//            Fila<Nodo> fila = new Fila<Nodo>();
//
//            fila.enfileira(this.raiz);
//            while (!fila.estaVazia()) {
//
//                Nodo cursor = fila.desenfileira();
//
//                System.out.print(cursor.elemento + " ");
//
//                if (cursor.esquerdo != null) {
//                    fila.enfileira(cursor.esquerdo);
//                }
//
//                if (cursor.direito != null) {
//                    fila.enfileira(cursor.direito);
//                }
//            }
//
//            System.out.println();
//
//        }

        public void imprimePreOrdem() {
            this.preOrdem(this.raiz);
            System.out.println();
        }

        public void imprimePosOrdem() {
            this.posOrdem(this.raiz);
            System.out.println();
        }

        public void imprimeEmOrdem() {
            this.emOrdem(this.raiz);
        }

        public void preOrdem(Nodo nodo) {

            if (nodo == null)
                return;

            System.out.print(nodo.chave + " ");
            this.preOrdem(nodo.esquerdo);
            this.preOrdem(nodo.direito);
        }

        public void posOrdem(Nodo nodo) {

            if (nodo == null)
                return;

            this.posOrdem(nodo.esquerdo);
            this.posOrdem(nodo.direito);
            System.out.print(nodo.chave + " ");
        }

        public void emOrdem(Nodo nodo) {

            if (nodo == null)
                return;

            this.emOrdem(nodo.esquerdo);
            System.out.print(nodo.chave + " ");
            nodo.ocorrencias.imprime();
            System.out.println();
            this.emOrdem(nodo.direito);
        }

        public void insere(String chave) {
            this.insere(chave, this.raiz);
        }

        public void insere(String chave, Nodo nodo) {

            Nodo novo = new Nodo(chave);

            if (nodo == null) {
                this.raiz = novo;
                this.nElementos++;
                return;
            }

            if (chave.compareTo(nodo.chave) < 0) {
                if (nodo.esquerdo == null) {
                    nodo.esquerdo = novo;
                    this.nElementos++;
                    return;
                } else {
                    this.insere(chave, nodo.esquerdo);
                }
            }

            if (chave.compareTo(nodo.chave) > 0) {
                if (nodo.direito == null) {
                    nodo.direito = novo;
                    this.nElementos++;
                    return;
                } else {
                    this.insere(chave, nodo.direito);
                }
            }
        }

        private Nodo maiorElemento(Nodo nodo) {
            while (nodo.direito != null) {
                nodo = nodo.direito;
            }
            return nodo;
        }

        private Nodo menorElemento(Nodo nodo) {
            while (nodo.esquerdo != null) {
                nodo = nodo.esquerdo;
            }
            return nodo;
        }

//        public boolean remove(int elemento) {
//            return this.remove(elemento, this.raiz) != null;
//        }

//        private Nodo remove(int elemento, Nodo nodo) {
//
//            if (nodo == null) {
//                System.out.println("Valor não encontrado");
//                return null;
//            }
//
//            if (elemento < nodo.chave) {
//                nodo.esquerdo = this.remove(elemento, nodo.esquerdo);
//            } else if (elemento > nodo.chave) {
//                nodo.direito = this.remove(elemento, nodo.direito);
//            } else {
//
////	    	if(nodo.esquerdo == null && nodo.direito == null) {
////	    		return null;
////	    	}
//
//                if (nodo.esquerdo == null) {
//                    this.nElementos--;
//                    return nodo.direito;
//                } else if (nodo.direito == null) {
//                    this.nElementos--;
//                    return nodo.esquerdo;
//                } else {
//                    Nodo substituto = this.menorElemento(nodo.direito);
//                    nodo.chave = substituto.chave;
//                    this.remove(substituto.chave, nodo.direito);
//                }
//            }
//
//            return nodo;
//        }

//        public boolean busca(String elemento) {
//            return this.busca(elemento, this.raiz);
//
//        }
//
//        public boolean busca(int elemento, Nodo nodo) {
//
//            if (nodo == null) {
//                return false;
//            }
//
//            if (elemento < nodo.chave) {
//                return this.busca(elemento, nodo.esquerdo);
//            } else if (elemento > nodo.chave) {
//                return this.busca(elemento, nodo.direito);
//            } else {
//                return true;
//            }
//        }

        public Nodo buscaNo(String chave) {
            return buscaNo(chave, this.raiz);
        }

        private Nodo buscaNo(String chave, Nodo nodo) {
            if (nodo == null) {
                return null;
            }

            int comparacao = chave.compareTo(nodo.chave);

            if (comparacao < 0) {
                return buscaNo(chave, nodo.esquerdo);
            } else if (comparacao > 0) {
                return buscaNo(chave, nodo.direito);
            } else {
                return nodo;
            }
        }

        private int altura(Nodo nodo) {

            if (nodo == null) {
                return -1;
            }

            int alturaEsquerda = this.altura(nodo.esquerdo) + 1;
            int alturaDireita = this.altura(nodo.direito) + 1;

            int altura = alturaEsquerda > alturaDireita ? alturaEsquerda : alturaDireita;

            return altura;

        }

        public int altura() {
            return this.altura(this.raiz);
        }
    }
}
