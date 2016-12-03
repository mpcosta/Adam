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
	
	// TODO: Add the static image to the resources package

//	private static final Image STATIC_IMAGE = new Image("adam/resources/static.png");
	private static final Image SPEAKING_IMAGE = new Image("adam/view/res/speaking_animation.png");
	private static final Image LISTENING_IMAGE = new Image("adam/view/res/ears_and_eyes.png");
	private static final int FRAMES_SPEAKING_IMAGE = 4;
	private static final int FRAME_LISTENING_IMAGE = 16;

	private ImageView staticImageView;
	private ImageView listeningImageView;
	private ImageView speakingImageView;
	
	private Scene scene;

	public Avatar(Scene scene) {
		this.scene = scene;
		
//		staticImageView = new ImageView(STATIC_IMAGE);
		listeningImageView = createAnimatedImageView(SPEAKING_IMAGE, 601, 582, FRAMES_SPEAKING_IMAGE, 450);
		speakingImageView = createAnimatedImageView(LISTENING_IMAGE, 601, 582, FRAME_LISTENING_IMAGE, 700);
	}

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
	
	private void addButtonCapabilities(ImageView imageView) {
		imageView.hoverProperty().addListener(handler -> {
			if (((ReadOnlyBooleanProperty)handler).getValue()) {
				scene.setCursor(Cursor.HAND);
			} else {
				scene.setCursor(Cursor.DEFAULT);
			}
		});
	}
	
	public ImageView getListeningImageView() {
		return listeningImageView;
	}
	
	public ImageView speakingImageView() {
		return speakingImageView;
	}
	
	public ImageView getStaticImageView() {
		return staticImageView;
	}

	public class SpriteAnimation extends Transition {
		private final ImageView imageView;
		private final int columns;
		private final int width;
		private final int height;
		private int lastIndex;

		public SpriteAnimation(ImageView imageView, Duration duration, int columns, int width, int height) {
			this.imageView = imageView;
			this.columns = columns;
			this.width = width;
			this.height = height;
			setCycleDuration(duration);
			setInterpolator(Interpolator.LINEAR);
		}

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
}
