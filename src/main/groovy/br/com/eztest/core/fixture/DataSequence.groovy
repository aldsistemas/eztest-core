package br.com.eztest.core.fixture

class DataSequence extends DataGenerator{

	int count = 0
	String prefix = ''
	String suffix = ''

	@Override
	def next() {
		def next = count++;
		prefix+next+suffix
	}
	
}
