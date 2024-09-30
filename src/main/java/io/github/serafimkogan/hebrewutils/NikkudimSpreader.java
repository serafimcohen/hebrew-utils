package io.github.serafimkogan.hebrewutils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class NikkudimSpreader {
	private static final String ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE = "Unable to spread diacritics from given source, most likely completely different words have been provided";
	private static final String NULL_POINTER_EXCEPTION_MESSAGE = "Given arguments shouldn't be null";
	
	private String reference;
    private String billet;
	private ArrayList<Change> changes = new ArrayList<>();
	private String result;
     
    public NikkudimSpreader(String reference, String billet) {
    	if (reference == null || billet == null)
    		throw new NullPointerException(NULL_POINTER_EXCEPTION_MESSAGE);  	
    	
    	this.reference = reference;
    	this.billet = billet;

		TokenList referenceTokens = new TokenList(this.reference, false);
		TokenList billetTokens = new TokenList(this.billet, false);
		
		if (billetTokens.size() != referenceTokens.size()) 
			throw new IllegalArgumentException(ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE);
			
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < billetTokens.size(); i++) {
			Change change = new Change(referenceTokens.get(i), billetTokens.get(i));
    		changes.add(change);
    		builder.append(change.after);
		}
		
		result = builder.toString();
    }
    
    public String getResult() {
        return result;
    }
    
    public String getBillet() {
        return billet;
    }
    
    public String getReference() {
        return reference;
    }
    
	public String getMetaData() {
		StringBuilder builder = new StringBuilder();
		
		for (Change change : changes) {
			builder.append(change.getMetaData());
			builder.append(Symbols.next_line);
		}
			
		builder.append("before: ");
		builder.append(billet);
		builder.append(Symbols.next_line);

		builder.append("after: ");
		builder.append(result);
		builder.append(" | cyrillization: ");
		builder.append(new Cyrillizer(result).getResult());
		
		builder.append(Symbols.next_line);
		
		builder.append("reference: ");
		builder.append(reference);
		builder.append(" | cyrillization: ");
		builder.append(new Cyrillizer(reference).getResult());

		return builder.toString();
	}

	private enum Sequence {
		VAV("ו"),
		YUD("י"),
		NULL(""),
		YUDYUD("יי"),
		VAVYUD("וי"),
		YUDVAV("יו"),
		VAVVAV("וו"),
		YUDVAVVAV("יוו"),
		VAVYUDVAV("ויו"),
		VAVYUDYUD("ויי"),
		VAVVAVYUD("ווי"),
		YUDVAVYUD("יוי"),
		YUDVAVVAVYUD("יווי"),
		YUDVAVYUDVAV("יויו"),
		VAVVAVYUDVAV("וויו"),
		VAVVAVYUDYUD("וויי"),
		OTHER("");
		
		final String sequence;
		Sequence(String sequence) {
			this.sequence = sequence;
		}
		
		static Sequence getSequence(String string) {
			for (Sequence sequence : values()) {
				if (sequence.sequence.equals(string)) {
					return sequence;
				}
			}
			
			return OTHER;
		}
	}
	
	private enum ChangeReason {
    	REGULAR(Sequence.NULL, Sequence.NULL),
    	VAV_TO_NULL(Sequence.VAV, Sequence.NULL),
    	YUD_TO_NULL(Sequence.YUD, Sequence.NULL),
    	NULL_TO_YUD(Sequence.NULL, Sequence.YUD),
    	YUDYUD_TO_YUD(Sequence.YUDYUD, Sequence.YUD),
    	NULL_TO_YUDYUD(Sequence.NULL, Sequence.YUDYUD),
    	YUD_TO_YUDYUD(Sequence.YUD, Sequence.YUDYUD),
    	VAV_TO_YUDVAV(Sequence.VAV, Sequence.YUDVAV),
    	YUD_TO_YUDVAV(Sequence.YUD, Sequence.YUDVAV),
    	VAVYUD_TO_YUDVAVYUD(Sequence.VAVYUD, Sequence.YUDVAVYUD),
    	YUDVAV_TO_YUDVAVYUD(Sequence.YUDVAV, Sequence.YUDVAVYUD),
    	VAVVAV_TO_YUDVAVVAV(Sequence.VAVVAV, Sequence.YUDVAVVAV),
    	VAVYUD_TO_VAVVAVYUD(Sequence.VAVYUD, Sequence.VAVVAVYUD),
    	VAV_TO_YUDVAVVAV(Sequence.VAV, Sequence.YUDVAVVAV),
    	YUDVAVVAV_TO_YUDVAVYUDVAV(Sequence.YUDVAVVAV, Sequence.YUDVAVYUDVAV),
    	YUDVAVVAVYUD_TO_YUDVAVYUD(Sequence.YUDVAVVAVYUD, Sequence.YUDVAVYUD),
    	NULL_TO_VAV(Sequence.NULL, Sequence.VAV),
    	VAVVAV_TO_VAV(Sequence.VAVVAV, Sequence.VAV),
    	YUDVAV_TO_VAV(Sequence.YUDVAV, Sequence.VAV),
    	NULL_TO_VAVVAV(Sequence.NULL, Sequence.VAVVAV),
    	VAV_TO_VAVVAV(Sequence.VAV, Sequence.VAVVAV),
    	YUD_TO_VAVYUDYUD(Sequence.YUD, Sequence.VAVYUDYUD),
    	YUD_TO_VAVYUD(Sequence.YUD, Sequence.VAVYUD),
    	YUDVAV_TO_VAVYUDVAV(Sequence.YUDVAV, Sequence.VAVYUDVAV),
    	YUDVAV_TO_VAVVAVYUD(Sequence.YUDVAV, Sequence.VAVVAVYUD),
    	VAV_TO_VAVVAVYUD(Sequence.VAV, Sequence.VAVVAVYUD),
    	VAVYUDVAV_TO_VAVVAVYUDVAV(Sequence.VAVYUDVAV, Sequence.VAVVAVYUDVAV),
		VAVYUDYUD_TO_VAVVAVYUDYUD(Sequence.VAVYUDYUD, Sequence.VAVVAVYUDYUD),
		VAVYUD_TO_VAVVAVYUDYUD(Sequence.VAVYUD, Sequence.VAVVAVYUDYUD),
		VAVVAVYUD_TO_VAVVAVYUDYUD(Sequence.VAVVAVYUD, Sequence.VAVVAVYUDYUD),
    	OTHER(Sequence.NULL, Sequence.NULL);

		final Sequence from; 
		final Sequence to; 
		ChangeReason(Sequence from, Sequence to) {
			this.from = from;
			this.to = to;
		}

		static ChangeReason getChangeReason(Sequence from, Sequence to) {
			for (ChangeReason reason : values()) {
				if (from == reason.from && to == reason.to) {
					return reason;
				}
			}
			
			return OTHER;
		}
	}
	
	@SuppressWarnings("serial")
	private static class TokenList extends ArrayList<Token> {
		private String source;
	    private String stringToTokenize;
	    private HashMap<Integer, String> tokens = new HashMap<>();
	    
	    private TokenList(String stringToTokenize, boolean separateYudsAndVavs) {
	    	this.source = stringToTokenize;
	        this.stringToTokenize = stringToTokenize
	                .replace(Symbols.Hebrew.abbreviation_two_gereshs, String.valueOf(Symbols.dot))
	                .replace(Symbols.Hebrew.abbreviation_two_apostrophes, String.valueOf(Symbols.dot))
	                .replace(Symbols.Hebrew.abbreviation_quotation_mark, Symbols.dot)
	                .replace(Symbols.Hebrew.abbreviation_gershayim, Symbols.dot)
	                .replace("-", "־");

	        tokenizePredefinedCombination(" ");
	        tokenizePredefinedCombination(".");
	        tokenizePredefinedCombination("־");
	        tokenizeLetters(separateYudsAndVavs);
	        tokenizeDigits();

	        generateTokensSet();
		}
		
		public String toString() {
			StringBuilder builder = new StringBuilder();
			for (Token token : this) 
				builder.append(token.toString());
			
			return builder.toString();
		}
		
		private String toStringAsTokenList() {
			StringBuilder builder = new StringBuilder();
			for (Token token : this) 
				builder.append(token.toStringAsToken());
			
			return builder.toString();
		}
		
		private void generateTokensSet() {
	        for (int i = 0; i < stringToTokenize.length(); i++) 
	            if (tokens.get(i) != null) 
	            	this.add(new Token(tokens.get(i)));
	    }
		
	    private void tokenizePredefinedCombination(String combination) {
	        if (!stringToTokenize.contains(combination))
	            return;
	    	
	        for (int i = 0; i < stringToTokenize.length() - combination.length() + 1; i++) {
	            if (stringToTokenize.substring(i, i + combination.length()).equals(combination)) {
	                tokens.put(i, combination);
	            }
	        }

	        StringBuilder spaceBuilder = new StringBuilder();
	        for (int i = 0; i < combination.length(); i++)
	            spaceBuilder.append(" ");
	        stringToTokenize = stringToTokenize.replace(combination, spaceBuilder);
	    }
	    
	    private void tokenizeLetters(boolean separateYudsAndVavs) {
	        for (int i = 0; i < stringToTokenize.length(); i++) {
	            if (Symbols.Hebrew.isHebrewLetter(stringToTokenize.charAt(i))) {

	                StringBuilder builder = new StringBuilder();
	                builder.append(stringToTokenize.charAt(i));

	                if (i == stringToTokenize.length() - 1) {
	                    tokens.put(i, builder.toString());
	                } else {
	                    for (int j = i + 1; j <= stringToTokenize.length(); j++) {
	                        if (j != stringToTokenize.length()
	                                && (Symbols.Hebrew.isHebrewDiacritics(stringToTokenize.charAt(j))
	                                || stringToTokenize.charAt(j) == Symbols.Hebrew.geresh
	                                || stringToTokenize.charAt(j) == Symbols.apostrophe
	                                || !separateYudsAndVavs && stringToTokenize.charAt(j) == 'ו'
	                                || !separateYudsAndVavs && stringToTokenize.charAt(j) == 'י')) {
	                            builder.append(stringToTokenize.charAt(j));
	                        } else {
	                            tokens.put(i, builder.toString());
	                            i = j - 1;
	                            break;
	                        }
	                    }
	                }
	            }
	        }
	    }

	    private void tokenizeDigits() {
	        for (int i = 0; i < stringToTokenize.length(); i++) {
	            if (Character.isDigit(stringToTokenize.charAt(i))) {

	                StringBuilder builder = new StringBuilder();
	                builder.append(stringToTokenize.charAt(i));

	                if (i == stringToTokenize.length() - 1) {
	                    tokens.put(i, builder.toString());
	                } else {
	                    for (int j = i + 1; j <= stringToTokenize.length(); j++) {
	                        if (j == stringToTokenize.length() || !Character.isDigit(stringToTokenize.charAt(j))) {
	                            tokens.put(i, builder.toString());
	                            i = j - 1;
	                            break;
	                        } else {
	                            builder.append(stringToTokenize.charAt(j));
	                        }
	                    }
	                }
	            }
	        }
	    }

	}
	
	private static class Token {
        private String root;
        private String fullToken;
        private ArrayList<Character> nikkudim = new ArrayList<>();
        private boolean isHebrewToken = false;
        
        private Token(String string) {
        	nikkudim.clear();
        	
            fullToken = string;

            StringBuilder rootBuilder = new StringBuilder();
            char firstChar = string.charAt(0);
            if (Symbols.Hebrew.isHebrewLetter(firstChar)) {
                isHebrewToken = true;
                
                for (char ch : string.toCharArray()) {
                    if (Symbols.Hebrew.isHebrewLetter(ch)) {
                        rootBuilder.append(ch);
                    } else {
                        nikkudim.add(ch);
                    }
                }
                root = rootBuilder.toString();
            } else {
                root = fullToken;
            }
            
            Collections.sort(nikkudim);
        }
        
        public String toString() {
        	StringBuilder builder = new StringBuilder();
        	
        	builder.append(this.root);
			for (char nikkud : this.nikkudim)
				builder.append(nikkud);
			
			return builder.toString();
        }
        
        private String toStringAsToken() {
        	StringBuilder builder = new StringBuilder();
        	
        	builder.append("[");
			builder.append(this.toString());
			builder.append("]");
			
			return builder.toString();
        }
    }
	
	private static class Change {
		private ChangeReason reason = ChangeReason.OTHER;
		private String before;
		private String beforeAsTokenList;
		private String after;
		private String afterAsTokenList;
		private String reference;
		private String referenceAsTokenList;
		
		private Change(Token referenceToken, Token billetToken) {
			TokenList billetTokenList = new TokenList(billetToken.fullToken, true);
			TokenList referenceTokenList = new TokenList(referenceToken.fullToken, true);
		    
			if (billetTokenList.get(0).isHebrewToken && !billetTokenList.get(0).root.equals(referenceTokenList.get(0).root)) {
				throw new IllegalArgumentException(ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE
						+ ", token \"" + billetTokenList.get(0).root + "\""
						+ " doesn't match with token \"" + referenceTokenList.get(0).root + "\"");
			}
			
			this.before = billetTokenList.toString();
			this.beforeAsTokenList = billetTokenList.toStringAsTokenList();
			this.reference = referenceTokenList.toString();
			this.referenceAsTokenList = referenceTokenList.toStringAsTokenList();
			
			String referenceWithoutDiacritics = Symbols.Hebrew.removeDiacritics(referenceTokenList.source);
			Sequence root = Sequence.getSequence(referenceWithoutDiacritics.substring(1).replace(String.valueOf(Symbols.apostrophe), ""));
			Sequence key = Sequence.getSequence(billetTokenList.source.substring(1).replace(String.valueOf(Symbols.apostrophe), ""));
			
			
			reason = ChangeReason.getChangeReason(root, key);
			
			if (referenceWithoutDiacritics.equals(billetTokenList.source)) {
				reason = ChangeReason.REGULAR;
				after = referenceToken.fullToken;
				afterAsTokenList = referenceToken.toStringAsToken();
			} else {
				switch (reason) {
					case REGULAR: {
						return;
					}
					case VAV_TO_NULL: {
						billetTokenList.get(0).nikkudim.addAll(referenceTokenList.get(0).nikkudim);
						billetTokenList.get(0).nikkudim.addAll(referenceTokenList.get(1).nikkudim);
						break;
					}
					case YUD_TO_NULL: {
						billetTokenList.get(0).nikkudim.addAll(referenceTokenList.get(0).nikkudim);
						if (!billetTokenList.get(0).nikkudim.contains(Symbols.Hebrew.hiriq))
							billetTokenList.get(0).nikkudim.add(Symbols.Hebrew.hiriq);
						break;
					}
					case NULL_TO_YUD: {
						break;
					}
					case YUDYUD_TO_YUD: {
						billetTokenList.get(0).nikkudim.addAll(referenceTokenList.get(0).nikkudim);
						break;
					}
					case NULL_TO_YUDYUD: {
						billetTokenList.get(0).nikkudim.addAll(referenceTokenList.get(0).nikkudim);
						break;
					}
					case YUD_TO_YUDYUD: {
						billetTokenList.get(0).nikkudim.addAll(referenceTokenList.get(0).nikkudim);
						billetTokenList.get(2).nikkudim.addAll(referenceTokenList.get(1).nikkudim);
						break;
					}
					case VAV_TO_YUDVAV: {
						billetTokenList.get(0).nikkudim.addAll(referenceTokenList.get(0).nikkudim);
						billetTokenList.get(2).nikkudim.addAll(referenceTokenList.get(1).nikkudim);	
						billetTokenList.get(2).nikkudim.removeIf(ch -> (ch == Symbols.Hebrew.hiriq));
						break;
					}
					case YUD_TO_YUDVAV: {
						billetTokenList.get(0).nikkudim.addAll(referenceTokenList.get(0).nikkudim);
						billetTokenList.get(2).nikkudim.addAll(referenceTokenList.get(1).nikkudim);
						billetTokenList.get(2).nikkudim.removeIf(ch -> (ch == Symbols.Hebrew.dagesh));
						break;
					}
					case VAVYUD_TO_YUDVAVYUD: {
						billetTokenList.get(0).nikkudim.addAll(referenceTokenList.get(0).nikkudim);
						billetTokenList.get(2).nikkudim.addAll(referenceTokenList.get(1).nikkudim);
						billetTokenList.get(3).nikkudim.addAll(referenceTokenList.get(2).nikkudim);
						break;
					}
					case YUDVAV_TO_YUDVAVYUD: {
						billetTokenList.get(0).nikkudim.addAll(referenceTokenList.get(0).nikkudim);
						billetTokenList.get(1).nikkudim.addAll(referenceTokenList.get(1).nikkudim);
						billetTokenList.get(2).nikkudim.addAll(referenceTokenList.get(2).nikkudim);
						break;
					}
					case VAVVAV_TO_YUDVAVVAV: {
						billetTokenList.get(0).nikkudim.addAll(referenceTokenList.get(0).nikkudim);
						billetTokenList.get(3).nikkudim.addAll(referenceTokenList.get(2).nikkudim);
						break;
					}
					case VAV_TO_YUDVAVVAV: {
						billetTokenList.get(0).nikkudim.addAll(referenceTokenList.get(0).nikkudim);
						billetTokenList.get(3).nikkudim.addAll(referenceTokenList.get(1).nikkudim);
						break;
					}
					case YUDVAVVAV_TO_YUDVAVYUDVAV: {
						billetTokenList.get(0).nikkudim.addAll(referenceTokenList.get(0).nikkudim);
						billetTokenList.get(2).nikkudim.addAll(referenceTokenList.get(1).nikkudim);
						billetTokenList.get(4).nikkudim.addAll(referenceTokenList.get(3).nikkudim);
						break;
					}
					case YUDVAVVAVYUD_TO_YUDVAVYUD: {
						billetTokenList.get(0).nikkudim.addAll(referenceTokenList.get(0).nikkudim);
						break;
					}
					case NULL_TO_VAV: {
						if (Symbols.Hebrew.nikkudimContainsNonStressedO(referenceTokenList.get(0).nikkudim)) {
							billetTokenList.get(1).nikkudim.add(Symbols.Hebrew.holam);
							if (referenceTokenList.get(0).nikkudim.contains(Symbols.Hebrew.dagesh))
								billetTokenList.get(0).nikkudim.add(Symbols.Hebrew.dagesh);
			            } else if (referenceTokenList.get(0).nikkudim.contains(Symbols.Hebrew.dagesh)) {
							billetTokenList.get(1).nikkudim.add(Symbols.Hebrew.dagesh);
			            }
						break;
					}
					case VAVVAV_TO_VAV: {
						billetTokenList.get(0).nikkudim.addAll(referenceTokenList.get(0).nikkudim);
						billetTokenList.get(1).nikkudim.addAll(referenceTokenList.get(2).nikkudim);
						break;
					}
					case YUDVAV_TO_VAV: {
						billetTokenList.get(0).nikkudim.addAll(referenceTokenList.get(0).nikkudim);
						billetTokenList.get(0).nikkudim.add(Symbols.Hebrew.hiriq);
						billetTokenList.get(1).nikkudim.addAll(referenceTokenList.get(2).nikkudim);
						break;
					}
					case NULL_TO_VAVVAV: {
						if (Symbols.Hebrew.nikkudimContainsNonStressedO(referenceTokenList.get(0).nikkudim)) {
							billetTokenList.get(1).nikkudim.add(Symbols.Hebrew.holam);
							billetTokenList.get(2).nikkudim.add(Symbols.Hebrew.holam);
							if (referenceTokenList.get(0).nikkudim.contains(Symbols.Hebrew.dagesh))
								billetTokenList.get(0).nikkudim.add(Symbols.Hebrew.dagesh);
			            } else if (referenceTokenList.get(0).nikkudim.contains(Symbols.Hebrew.dagesh)) {
							billetTokenList.get(1).nikkudim.add(Symbols.Hebrew.dagesh);
							billetTokenList.get(2).nikkudim.add(Symbols.Hebrew.dagesh);
			            }
						break;
					}
					case VAV_TO_VAVVAV: {
						billetTokenList.get(0).nikkudim.addAll(referenceTokenList.get(0).nikkudim);
						billetTokenList.get(2).nikkudim.addAll(referenceTokenList.get(1).nikkudim);
						break;
					}
					case YUD_TO_VAVYUDYUD: {
						if (Symbols.Hebrew.nikkudimContainsNonStressedO(referenceTokenList.get(0).nikkudim)) {
							billetTokenList.get(1).nikkudim.add(Symbols.Hebrew.holam);
							if (referenceTokenList.get(0).nikkudim.contains(Symbols.Hebrew.dagesh))
								billetTokenList.get(0).nikkudim.add(Symbols.Hebrew.dagesh);
			            } else if (referenceTokenList.get(0).nikkudim.contains(Symbols.Hebrew.dagesh)) {
							billetTokenList.get(1).nikkudim.add(Symbols.Hebrew.dagesh);
			            }
						billetTokenList.get(2).nikkudim.addAll(referenceTokenList.get(1).nikkudim);
						break;
					}
					case YUD_TO_VAVYUD: {
						if (Symbols.Hebrew.nikkudimContainsNonStressedO(referenceTokenList.get(0).nikkudim)) {
							billetTokenList.get(1).nikkudim.add(Symbols.Hebrew.holam);
							if (referenceTokenList.get(0).nikkudim.contains(Symbols.Hebrew.dagesh))
								billetTokenList.get(0).nikkudim.add(Symbols.Hebrew.dagesh);
			            } else if (referenceTokenList.get(0).nikkudim.contains(Symbols.Hebrew.dagesh)) {
							billetTokenList.get(1).nikkudim.add(Symbols.Hebrew.dagesh);
			            }
						billetTokenList.get(2).nikkudim.addAll(referenceTokenList.get(1).nikkudim);
						break;
					}
					case YUDVAV_TO_VAVYUDVAV: {
						if (Symbols.Hebrew.nikkudimContainsNonStressedO(referenceTokenList.get(0).nikkudim)) {
							billetTokenList.get(1).nikkudim.add(Symbols.Hebrew.holam);
							if (referenceTokenList.get(0).nikkudim.contains(Symbols.Hebrew.dagesh))
								billetTokenList.get(0).nikkudim.add(Symbols.Hebrew.dagesh);
			            } else if (referenceTokenList.get(0).nikkudim.contains(Symbols.Hebrew.dagesh)) {
							billetTokenList.get(1).nikkudim.add(Symbols.Hebrew.dagesh);
			            }
						billetTokenList.get(2).nikkudim.addAll(referenceTokenList.get(1).nikkudim);
						billetTokenList.get(3).nikkudim.addAll(referenceTokenList.get(2).nikkudim);
						break;
					}
					case YUDVAV_TO_VAVVAVYUD: {
						billetTokenList.get(0).nikkudim.addAll(referenceTokenList.get(0).nikkudim);
						billetTokenList.get(2).nikkudim.addAll(referenceTokenList.get(1).nikkudim);
						billetTokenList.get(3).nikkudim.addAll(referenceTokenList.get(2).nikkudim);
						break;
					}
					case VAV_TO_VAVVAVYUD: {
						billetTokenList.get(0).nikkudim.addAll(referenceTokenList.get(0).nikkudim);
						billetTokenList.get(2).nikkudim.addAll(referenceTokenList.get(1).nikkudim);
						break;
					}
					case VAVYUDVAV_TO_VAVVAVYUDVAV: {
						billetTokenList.get(0).nikkudim.addAll(referenceTokenList.get(0).nikkudim);
						billetTokenList.get(2).nikkudim.addAll(referenceTokenList.get(1).nikkudim);
						billetTokenList.get(3).nikkudim.addAll(referenceTokenList.get(2).nikkudim);
						billetTokenList.get(4).nikkudim.addAll(referenceTokenList.get(3).nikkudim);
						break;
					}
					case VAVYUDYUD_TO_VAVVAVYUDYUD: {
						billetTokenList.get(0).nikkudim.addAll(referenceTokenList.get(0).nikkudim);
						billetTokenList.get(2).nikkudim.addAll(referenceTokenList.get(1).nikkudim);
						billetTokenList.get(3).nikkudim.addAll(referenceTokenList.get(2).nikkudim);
						billetTokenList.get(4).nikkudim.addAll(referenceTokenList.get(3).nikkudim);
						break;
					}
					case VAVYUD_TO_VAVVAVYUDYUD: {
						billetTokenList.get(0).nikkudim.addAll(referenceTokenList.get(0).nikkudim);
						billetTokenList.get(2).nikkudim.addAll(referenceTokenList.get(1).nikkudim);
						billetTokenList.get(4).nikkudim.addAll(referenceTokenList.get(2).nikkudim);
						break;
					}
					case VAVVAVYUD_TO_VAVVAVYUDYUD: {
						billetTokenList.get(0).nikkudim.addAll(referenceTokenList.get(0).nikkudim);
						billetTokenList.get(1).nikkudim.addAll(referenceTokenList.get(1).nikkudim);
						billetTokenList.get(2).nikkudim.addAll(referenceTokenList.get(2).nikkudim);
						billetTokenList.get(3).nikkudim.addAll(referenceTokenList.get(3).nikkudim);
						break;
					}
					case VAVYUD_TO_VAVVAVYUD: {
						billetTokenList.get(0).nikkudim.addAll(referenceTokenList.get(0).nikkudim);
						billetTokenList.get(2).nikkudim.addAll(referenceTokenList.get(1).nikkudim);
						billetTokenList.get(3).nikkudim.addAll(referenceTokenList.get(2).nikkudim);
						break;
					}
					default: {
						
					}
				}
				
				after = billetTokenList.toString();
				afterAsTokenList = billetTokenList.toStringAsTokenList();
			}
		}
    	
		private String getMetaData() {
    		StringBuilder builder = new StringBuilder();
    		builder.append(reason.name());
    		
    		builder.append(" | before: \"");
    		builder.append(before);
    		builder.append("\" (");
    		builder.append(beforeAsTokenList);
    		builder.append(")");
    		
    		builder.append(" | after: \"");
    		builder.append(after);
    		builder.append("\" (");
    		builder.append(afterAsTokenList);
    		builder.append(")");
    		
    		builder.append(" | reference: \"");
    		builder.append(reference);
    		builder.append("\" (");
    		builder.append(referenceAsTokenList);
    		builder.append(")");
    		
    		return builder.toString();
    	}
	}
    
    public static void main(String[] args) {
    	NikkudimSpreader spreader = new NikkudimSpreader("טַיָּסֵיכֶם", "טייסיכם");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("טַיֶּסֶתָהּ", "טייסתה");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("טַיֶּסֶתָן", "טייסתן");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("כַּלְבוֹתַיִךְ", "כלבותייך");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("שַׁמְרָנַי", "שמרניי");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("אֲבוֹתַי", "אבותיי");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("אָזְנֵי", "אוזני");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("אֵיבוֹתַי", "איבותיי");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("אֲסֵפַתְכֶם", "אסיפתכם");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("בִּכּוּרַיִךְ", "בכורייך");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("גְּבִינוֹתַי", "גבינותיי");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("בָּנַיִךְ", "בנייך");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("בְּכוֹרַיִךְ", "בכורייך");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("גְּמַלַּיִךְ", "גמלייך");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("דִּבְשָׁהּ", "דובשה");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("דְּלָתַיִם", "דלתיים");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("דְּחָפַיִךְ", "דחפייך");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("הַהֻלֶּדֶת", "ההולדת");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("הִזְדַּמְּנוּיוֹתַי", "הזדמנויותיי");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("זְרָמַיִךְ", "זרמייך");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("חֹטֶם", "חוטם");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("חָטְמְכֶם", "חוטמכם");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("חוֹפַיִךְ", "חופייך");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("חִכֵּנוּ", "חיכנו");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("חֲנֻכִּיָּתָן", "חנוכייתן");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("חֲנֻכִּיּוֹתָן", "חנוכיותן");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("חֻקָּתָן", "חוקתן");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("זִכְרוֹנוֹתֵיכֶם", "זיכרונותיכם");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("טֶכְנוֹלוֹגְיָתְךָ", "טכנולוגייתך");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("טֶכְנוֹלוֹגְיוֹתַי", "טכנולוגיותיי");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("כַּפּוֹתַי", "כפותיי");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("כְּתָרַי", "כתריי");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("כְּנָפַי", "כנפיי");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("מִבְטָאַי", "מבטאיי");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("מָגִנָּהּ", "מגינה");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("מָגִנֵּיהֶם", "מגיניהם");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("מִלַּי", "מיליי");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("מַכְשִׁירַיִךְ", "מכשירייך");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("נְזִידַי", "נזידיי");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("מִשְׁפְּחוֹתַי", "משפחותיי");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("נְשָׁרַיִךְ", "נשרייך");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("נְסָכַיִךְ", "נסכייך");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("סָבַי", "סביי");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("נִגּוּנֵיכֶם", "ניגוניכם");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("נִסְּכֶן", "ניסכן");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("נִסֵּיכֶם", "ניסיכם");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("נִסֵּיכֶן", "ניסיכן");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("סְפִינוֹתַי", "ספינותיי");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("עֹז", "עוז");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("עֻזְּכֶם", "עוזכם");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("עַפְעַפַּיִךְ", "עפעפייך");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("פְּלָגַי", "פלגיי");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("צָרְכֵּנוּ", "צורכנו");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("צַמּוֹתַיִךְ", "צמותייך");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("קָשְׁיָן", "קושין");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("הַקִּצּוּר", "הקיצור");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("רִבּוֹנִי", "ריבוני");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("רַגְלַיִם", "רגליים");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("שִׁחְזוּרַי", "שחזוריי");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("שְׁלָגַיִךְ", "שלגייך");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("שָׁמַיִךְ", "שמייך");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("שִׂמְחוֹתַיִךְ", "שמחותייך");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("שִׁנַּיִם", "שיניים");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("שַׁרְשְׁרָאוֹתַי", "שרשראותיי");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("שְׂרֵפוֹתַיִךְ", "שרפותייך");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("תְּפִלּוֹתֵיכֶן", "תפילותיכן");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("אַוָּזֵי", "אווזי");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("אֲוִירוֹת", "אווירות");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("אֹֽכֶל", "אוכל");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("אָרְנֵי", "אורני");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("אֹמֶץ", "אומץ");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("אִידֵאָל", "אידיאל");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("אִסּוּר", "איסור");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("אוֹפַנַּיִם", "אופניים");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("אַקְוַרְיוּם", "אקווריום");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("בֻּבָּה", "בובה");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("בַּכְיַן", "בכיין");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("בֵּיצִיָּה", "ביצייה");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("בָּתֵּי-מִשְׁפָּט", "בתי משפט");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("גֻּמִּיָּה", "גומיה");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("גֹּדֶל", "גודל");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("גִּלָּיוֹן", "גיליון");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("דֹּלֶב", "דולב");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("גִּתִּים", "גיתים");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("דֹּאַר אֵלֶקְטְרוֹנִי", "דואר אלקטרוני");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("גָּפְרִית", "גופרית");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("דַּיָּנֵי", "דייני");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("הֶשֵּׂג", "הישג");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("הִפּוּךְ", "היפוך");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("הַשְׂוָאוֹת", "השוואות");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("הַשֵּׁם הַמְּפֹרָשׁ", "השם המפורש");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("הִתְיַקְּרֻיּוֹת", "התייקרויות");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("וִכּוּחִים", "ויכוחים");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("זָוִיּוֹת", "זוויות");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("זִקּוֹת", "זיקות");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("חֶדְוַת", "חדוות");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("חֹל", "חול");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("חִתּוּלֵי", "חיתולי");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("חִטָּה", "חיטה");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("חִוּוּט", "חיווט");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("חֻרְבָּנוֹת", "חורבנות");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("חֻרְבְּנוֹת", "חורבנות");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("חֲצוֹת-הַלַּיְלָה", "חצות הלילה");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("טִגּוּן", "טיגון");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("יֹקֶר", "יוקר");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("יֹפִי", "יופי");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("כַּיָּס", "כייס");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("כִּפּוּרֵי", "כיפורי");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("כִּפּוֹת", "כיפות");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("כִּשָּׁרוֹן", "כישרון");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("כִּנּוֹר", "כינור");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("כְּלֵי-דָּם", "כלי דם");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("כַּרְכֻּמִּים", "כרכומים");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("לִבָּה", "ליבה");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("לֵדָה", "לידה");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("מֵדַלְיָה", "מדלייה");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("מוּזֵאוֹן", "מוזיאון");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("מֹתֶק", "מותק");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("מִסְתּוֹר", "מיסתור");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("מִצְרָכִים", "מיצרכים");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("מִמּוּנִים", "מימונים");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("מַלְכֹּדֶת", "מלכודת");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("מְסִבַת", "מסיבת");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("מַעֲרֶכֶת מְבֻדֶּדֶת", "מערכת מבודדת");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("מִסְפָּרַיִם", "מספריים");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("מַעֲשִׂיָּה", "מעשייה");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("מְשֻׁלַּשׁ", "משולש");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("נְקֻדָּה וּפְסִיק", "נקודה ופסיק");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("נִצָּב", "ניצב");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("סֻכְּרָזִית", "סוכרזית");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("סֻכָּר", "סוכר");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("סְחַרְחֹרֶת", "סחרחורת");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("סִבּוֹת", "סיבות");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("סִכּוּיֵי", "סיכויי");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("סְכֶמָת", "סכמות");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("עֲבֵרָה", "עבירה");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("עֹרְבִים", "עורבים");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("סְתָו", "סתיו");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("עִכּוּלִים", "עיכולים");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("סִיֹּמֶת", "סיומת");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("עֲקֵדָה", "עקידה");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("עֲשֶׂרֶת הַדִּבְּרוֹת", "עשרת הדיברות");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("עָשׂוֹר", "עישור");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("פֵּרוּר", "פירור");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("פִּנּוּי", "פינוי");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("פַּעֲמַיִם", "פעמיים");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("צִוְתֵי", "צוותי");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("קְנִיָּה", "קנייה");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("רֹאשׁ חֹדֶשׁ", "ראש חודש");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("קִנָּמוֹן", "קינמון");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("רִגּוּל תַּעֲשִׂיָּתִי", "ריגול תעשייתי");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("רֵאַת", "ריאת");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("שִׁמּוּר", "שימור");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("שְׁנִיָּה", "שנייה");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("שְׁתִיַּת", "שתיית");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("תַּאֲוָה", "תאווה");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("תֻּרְגְּמָן", "תורגמן");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("תִּסְמֹנֶת הָאַף הַלָּבָן", "תסמונת האף הלבן");
    	System.out.println(spreader.getMetaData());
    	spreader = new NikkudimSpreader("תִּקְוָה", "תקווה");
    	System.out.println(spreader.getMetaData());
    }
}
