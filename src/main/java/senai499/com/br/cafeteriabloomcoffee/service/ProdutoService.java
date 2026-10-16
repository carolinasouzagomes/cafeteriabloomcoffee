
package senai499.com.br.cafeteriabloomcoffee.service;

import java.util.List;

import org.springframework.stereotype.Service;

import senai499.com.br.cafeteriabloomcoffee.model.Produto;
import senai499.com.br.cafeteriabloomcoffee.repository.ProdutosRepository;

@Service
public class ProdutoService {

    private final ProdutosRepository produtoRepository;

    public ProdutoService(ProdutosRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public List<Produto> listarDisponiveis() {
        return produtoRepository.findByDisponivelTrue();
    }

    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    public Produto salvar(Produto produto) {
        return produtoRepository.save(produto);
    }

    public void excluir(Long id) {
        produtoRepository.deleteById(id);
    }
}
