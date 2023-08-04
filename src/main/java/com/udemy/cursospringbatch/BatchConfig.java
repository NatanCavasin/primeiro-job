package com.udemy.cursospringbatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableBatchProcessing //Permite que seja oinjetado os componentes do spring Batch
@Configuration
public class BatchConfig {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	
	@Bean
	public Job imprimeOlaJob() {
		return jobBuilderFactory
				.get("imprimeOlaJob") //Nome do Job
				.start(imprimeOlaStep()) //steps
				.incrementer(new RunIdIncrementer()) // Adiciona um run ID a acda execução, deste modo o Job pode ser executado mais de uma vez.
				.build();
		// O incrementador somente pode ser utilizado se o Job não precisa ser reinicializado
	}
	
	public Step imprimeOlaStep() {
		return stepBuilderFactory
				.get("imprimeOlaStep")
				.tasklet(imprimeOlaTasklet(null)) 
				.build();
	}

	@Bean
	@StepScope
	public Tasklet imprimeOlaTasklet(@Value("#{jobParameters['nome']}") String nome) {
		return new Tasklet() { //para comandos mais simples que não requere muito processamento dos dados
			
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				System.out.println(String.format("Olá %s! :)", nome));
				return RepeatStatus.FINISHED;
			}
		};
	}
}
