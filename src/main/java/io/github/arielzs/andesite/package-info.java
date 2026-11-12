/**
 * Pacote raiz da Andesite Hardware Co. Contém apenas o ponto de entrada da aplicação
 * ({@link io.github.arielzs.andesite.Main}); o restante do código fica nos subpacotes:
 * <ul>
 *   <li>{@code model}: entidades de domínio (produtos, máquinas, matéria-prima, demandas);</li>
 *   <li>{@code service}: regras de negócio e orquestração da produção;</li>
 *   <li>{@code strategy}: estratégias de escolha da próxima demanda (padrão Strategy);</li>
 *   <li>{@code exception}: exceções de regra de negócio da planta;</li>
 *   <li>{@code ui}: interface de terminal.</li>
 * </ul>
 */
package io.github.arielzs.andesite;
