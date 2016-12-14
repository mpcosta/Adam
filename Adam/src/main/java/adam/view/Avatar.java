package adam.view;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Avatar {
/**
	 * Variable which gets and calls the images of Adam 
	 * Speaking Image for the listening bars under Adam
	 * Listening image for Adam's ears and eyes 
	 * Static image for a frozen Adam
	 */
	private final Image SPEAKING_IMAGE = new Image("/speaking_animation.png");
	private final Image LISTENING_IMAGE = new Image("/ears_and_eyes.png");
	private final Image STATIC_IMAGE = new Image("/static_image.png");
	private static final int FRAMES_SPEAKING_IMAGE = 4;
	private static final int FRAME_LISTENING_IMAGE = 16;
	
	private Scene scene;
	
	private ImageView staticImage, speakingImage, listeningImage; 
/**
	 * A constructor for Adam 
	 * Resizes the images containing Adam 
	 * @param scene
	 */
	public Avatar(Scene scene) {
		this.scene = scene;
		
		initStaticImage();
		
		listeningImage = createAnimatedImageView(LISTENING_IMAGE, 601, 582, FRAMES_SPEAKING_IMAGE, 700);
		listeningImage.setFitHeight(90);
		speakingImage = createAnimatedImageView(SPEAKING_IMAGE, 601, 582, FRAME_LISTENING_IMAGE, 450);
		speakingImage.setFitHeight(90);
	}
/**
	 * Initialises the static image of Adam
	 */
	private void initStaticImage() {
		staticImage = new ImageView(STATIC_IMAGE);
		staticImage.setPreserveRatio(true);
		staticImage.setFitHeight(90);
		addButtonCapabilities(staticImage);
	}
/**
	 * Creates the animated image view for Adam 
	 * Sets the speaking animation for Adam 
	 * @param image
	 * @param frameWidth
	 * @param frameHeight
	 * @param framesNumber  - frames per second 
	 * @param duration
	 * @return the image of Adam 
	 */
	private ImageView createAnimatedImageView(Image image, int frameWidth, int frameHeight, int framesNumber,
			int duration) {
		final ImageView imageView = new ImageView(image);
		imageView.setFitWidth(300);
		imageView.setPreserveRatio(true);
		imageView.setViewport(new Rectangle2D(0, 0, frameWidth, frameHeight));

		final Animation speakingAnimation = new SpriteAnimation(imageView, Duration.millis(duration), framesNumber,
				frameWidth, frameHeight);
		speakingAnimation.setCycleCount(Animation.INDEFINITE);
		speakingAnimation.play();
		
		addButtonCapabilities(imageView);

		return imageView;
	}
	/**
	 * A method to change the cursor to a hand when hovered over Adam 
	 * @param imageView
	 */
	private void addButtonCapabilities(ImageView imageView) {
		imageView.hoverProperty().addListener(handler -> {
			if (((ReadOnlyBooleanProperty)handler).getValue()) {
				scene.setCursor(Cursor.HAND);
			} else {
				scene.setCursor(Cursor.DEFAULT);
			}
		});
	}
/**
	 * Initiates the animated image of Adam 
	 * Transitioning it from static to animation  
	 * @author hagerabdo
	 */
	private class SpriteAnimation extends Transition {
		private final ImageView imageView;
		private final int columns;
		private final int width;
		private final int height;
		private int lastIndex;
/**
		 * A sprite animation created and sets the cycle duration and interpolate
		 * @param imageView
		 * @param duration
		 * @param columns
		 * @param width
		 * @param height
		 */
		public SpriteAnimation(ImageView imageView, Duration duration, int columns, int width, int height) {
			this.imageView = imageView;
			this.columns = columns;
			this.width = width;
			this.height = height;
			setCycleDuration(duration);
			setInterpolator(Interpolator.LINEAR);
		}
/**
		 * A method to change the viewport accordingly to the number of columns and size of one image within a column 
		 * Changes the view port accordingly to the size of the image sequence 
		 */
		protected void interpolate(double k) {
			final int index = Math.min((int) Math.floor(k * columns), columns - 1);
			if (index != lastIndex) {
				final int x = (index % columns) * width;
				final int y = (index / columns) * height;
				imageView.setViewport(new Rectangle2D(x, y, width, height));
				lastIndex = index;
			}
		}
	}
/**
	 * Gets the static image 
	 * @return static image 
	 */
	public ImageView getStaticImage() {
		return staticImage;
	}
/**
	 * Gets the speaking image 
	 * @return the speaking image 
	 */
	public ImageView getSpeakingImage() {
		return speakingImage;
	}
/**
	 * Gets the listening image 
	 * @return the listening image 
	 */
	public ImageView getListeningImage() {
		return listeningImage;
	}
}
