package de.dhbw.caput.tinf20b1.thompson;

final class Parser {

	public static RegularExpression parse( String string ){
		// TODO: Customize the code here to create a regular expression from the given string.

		if (string.length() == 0) return new RegularExpression.EmptySet();

		if (string.length() == 1) {
			if (string.charAt(0) == 'ε') {
				return new RegularExpression.EmptyWord();
			}
			return new RegularExpression.Literal(string.charAt(0));
		}

		Symbol symbol = findLeastBindingSymbol(string);
		switch (symbol.symbol){
			case '|':
				return new RegularExpression.Union(parse(symbol.left), parse(symbol.right));
			case '·':
				return new RegularExpression.Concatenation(parse(symbol.left), parse(symbol.right));
			case '*':
				return new RegularExpression.KleeneStar(parse(symbol.left));
		}


		return new RegularExpression.EmptySet( );
	}

	public static Symbol findLeastBindingSymbol(String string){

		int currentSymbol = 10;
		int position = -1;
		int bracketCounter = 0;

		for (int i = 0; i < string.length(); i++){
			switch (string.charAt(i)){
				case '|':
					if (bracketCounter == 0) {
						if (currentSymbol > 1) {
							currentSymbol = 1;
							position = i;
						}
					}
					break;
				case '·':
					if (bracketCounter == 0){
						if (currentSymbol > 2) {
							currentSymbol = 2;
							position = i;
						}
				}
					break;
				case '*':
					if (bracketCounter == 0) {
						if (currentSymbol > 3) {
							currentSymbol = 3;
							position = i;
						}
					}
					break;
				case '(':
					bracketCounter++;
					break;
				case ')':
					bracketCounter--;
					break;
				default:
					break;
			}
		}

		if (currentSymbol == 10){
			string = string.substring(1, string.length() - 1);
			return findLeastBindingSymbol(string);
		}

		char symbol;
		switch (currentSymbol) {
			case 1:
				symbol = '|';
				break;
			case 2:
				symbol = '·';
				break;
			case 3:
				symbol = '*';
				break;
			default:
				symbol = ' ';
				break;
		}

		return new Symbol(string, position, symbol);
	}
}
